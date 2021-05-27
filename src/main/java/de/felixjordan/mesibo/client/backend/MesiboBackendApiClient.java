/* 
 * Copyright (c) BUDDY Activities UG (haftungsbeschränkt) - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential
 *
 * Written by Felix Jordan <felix.jordan@buddy-app.de>, May 2021
 */
package de.felixjordan.mesibo.client.backend;

import java.io.IOException;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.felixjordan.mesibo.client.MesiboApiException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Felix Jordan
 */
public class MesiboBackendApiClient implements MesiboBackendApi {

	/*
	 * NOTE: Although Mesibo call their API a 'REST API' it is NOT restful at all.
	 * All requests go to the same URL as an HTTP POST request with the operation id
	 * as a query parameter.
	 */

	public static final String API_ENDPOINT_URL = "https://api.mesibo.com/api.php";

	private static final Logger logger = LoggerFactory.getLogger(MesiboBackendApiClient.class);

	private static final RequestBody EMPTY_REQUEST_BODY = RequestBody.create(null, new byte[0]);

	// constants for operation IDs:
	private static final String OPERATION_USERADD = "useradd";

	/**
	 * The application token to use for authentication.
	 */
	private String appToken;

	private OkHttpClient httpClient;

	private ObjectMapper objectMapper;

	/**
	 * 
	 * @param appToken the application token to use for authentication
	 */
	MesiboBackendApiClient(String appToken) {
		this(appToken, new OkHttpClient(), createDefaultObjectMapper());
	}

	/**
	 * 
	 * @param apiKey
	 * @param httpClient
	 * @param objectMapper
	 */
	MesiboBackendApiClient(String appToken, OkHttpClient httpClient, ObjectMapper objectMapper) {
		Validate.notBlank(appToken);
		Validate.notNull(httpClient);
		Validate.notNull(objectMapper);
		this.appToken = appToken;
		this.httpClient = httpClient;
		this.objectMapper = objectMapper;
	}

	@Override
	public AddUserResult addUser(String userAddress, String appId, Integer expiry, Boolean active, String name,
			Integer maxGroups) throws MesiboApiException {
		Validate.notBlank(userAddress);
		Validate.notBlank(appId);

		HttpUrl.Builder url = HttpUrl.parse(API_ENDPOINT_URL).newBuilder()
				.addQueryParameter("addr", userAddress)
				.addQueryParameter("appid", appId);

		if (expiry != null) {
			url.addQueryParameter("expiry", expiry.toString());
		}
		if (active != null) {
			url.addQueryParameter("active", active.toString());
		}
		if (name != null) {
			url.addQueryParameter("name", name);
		}
		if (maxGroups != null) {
			url.addQueryParameter("groups", name);
		}

		return executeAuthenticatedOperation(OPERATION_USERADD, url, AddUserResult.class);
	}

	@Override
	public AddUserResult addUser(String userAddress, String appId) throws MesiboApiException {
		// method interprets 'null' for optional parameters as 'not present'
		return addUser(userAddress, appId, null, null, null, null);
	}

	/**
	 * Builds an authenticated request based on the {@link HttpUrl.Builder},
	 * executes it, and maps the response to the given response type.
	 * 
	 * @param urlBuilder   a builder prepared to build a Mesibo API request.
	 * @param responseType the class representing the response
	 * @return
	 * @throws MesiboApiException
	 */
	private <T extends BaseOperationResult> T executeAuthenticatedOperation(
			String operationId,
			HttpUrl.Builder urlBuilder,
			Class<T> responseType)
			throws MesiboApiException {
		Validate.notBlank(operationId);

		HttpUrl requestUrl = urlBuilder
				.addQueryParameter("op", operationId)
				.addQueryParameter("token", appToken)
				.build();

		logger.debug("executing Mesibo backend API request: {}", requestUrl);

		Request request = new Request.Builder()
				.post(EMPTY_REQUEST_BODY)
				.url(requestUrl)
				.build();

		T result = executeAndRead(request, responseType);

		// check for error
		if (result.isError()) {
			throw new MesiboErrorResponseException(result);
		}
		return result;
	}

	/**
	 * Executes the request and maps the response data to an instance of the given
	 * type.
	 * 
	 * @param <T>
	 * @param request
	 * @param valueType
	 * @return
	 * @throws MesiboApiException
	 */
	private <T> T executeAndRead(Request request, Class<T> valueType) throws MesiboApiException {
		Response response;
		try {
			response = this.httpClient.newCall(request).execute();
		} catch (IOException e) {
			logger.error("IOException when executing request", e);
			throw new MesiboApiException("IOException during request", e);
		}

		if (!response.isSuccessful()) {
			logger.debug("error response headers: {}", response.headers());
			try {
				logger.debug("error response body: {}", response.body().string());
			} catch (IOException e) {
				logger.warn("Faield to log error resposne body", e);
			}
			throw new MesiboApiException("Request error: " + response.code());
		}

		String rawResponseBody;
		try {
			rawResponseBody = response.body().string();
		} catch (IOException e) {
			throw new MesiboApiException("Failed to read response body", e);
		}

		logger.debug("raw response body: " + rawResponseBody);

		try {
			return this.objectMapper.readValue(rawResponseBody, valueType);
		} catch (JsonParseException e) {
			logger.error("Invalid JSON in response body", e);
			throw new MesiboApiException("Invalid JSON in response body", e);
		} catch (JsonMappingException e) {
			logger.error("Cannot map reponse to target type: " + valueType, e);
			throw new MesiboApiException("Cannot map response to target type: " + valueType, e);
		} catch (IOException e) {
			logger.error("IOException while mapping response body", e);
			throw new MesiboApiException("IOException while mapping response body", e);
		}
	}

	/**
	 * Creates the default {@link ObjectMapper} to be used by the client.
	 * 
	 * @return
	 */
	private static ObjectMapper createDefaultObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		/*
		 * Disable here so we can keep the classes in the domain
		 * module free from Jackson annotation
		 */
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		return objectMapper;
	}
}
