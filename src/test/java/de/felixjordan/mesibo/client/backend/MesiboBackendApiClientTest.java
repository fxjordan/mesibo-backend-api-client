/* 
 * Copyright (c) BUDDY Activities UG (haftungsbeschränkt) - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential
 *
 * Written by Felix Jordan <felix.jordan@buddy-app.de>, May 2021
 */
package de.felixjordan.mesibo.client.backend;

import org.apache.commons.lang3.Validate;

import de.felixjordan.mesibo.client.MesiboApiException;

/**
 * @author Felix Jordan
 */
public class MesiboBackendApiClientTest {

	/*
	 * TODO Turn into unit test
	 */

	public static void main(String[] args) {

		if (args.length < 1) {
			throw new IllegalArgumentException("test requires a Mesibo application token as first parameter");
		}
		String testAppToken = args[0];

		MesiboBackendApiClientTest testInstance = new MesiboBackendApiClientTest(testAppToken);
		testInstance.testCreateUser();

		// Paste UID of some existing Mesibo user here
		testInstance.testRegenerateToken("333635");
	}

	private String appToken;

	MesiboBackendApiClientTest(String appToken) {
		this.appToken = appToken;
	}

	private void testCreateUser() {
		// Create client
		MesiboBackendApi mesiboClient = new MesiboBackendApiClient(appToken);

		AddUserResult result;
		try {
			result = mesiboClient.addUser("my-test-user-addr", "com.example.someapp");
		} catch (MesiboApiException e) {
			throw new RuntimeException("Failed to add user", e);
		}
		String userId = result.getUser().getUid();
		String accessToken = result.getUser().getToken();

		System.out.println("Added test user to Mesibo: userId=" + userId + ", accessToken=" + accessToken);
	}

	private void testRegenerateToken(String uid) {
		// Create client
		MesiboBackendApi mesiboClient = new MesiboBackendApiClient(appToken);

		RegenerateAccessTokenResult result;
		try {
			result = mesiboClient.regenerateUserAccessToken(uid, "com.example.someapp");
		} catch (MesiboApiException e) {
			throw new RuntimeException("Failed regenerate user access token", e);
		}
		String userId = result.getUser().getUid();
		Validate.isTrue(userId.equals(uid), "returned uid should equal the uid from the request!");
		String accessToken = result.getUser().getToken();

		System.out.println("regenerated user access token for userId=" + userId + " : accessToken=" + accessToken);
	}
}
