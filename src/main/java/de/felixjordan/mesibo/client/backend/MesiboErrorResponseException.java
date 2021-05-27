/* 
 * Copyright (c) BUDDY Activities UG (haftungsbeschränkt) - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential
 *
 * Written by Felix Jordan <felix.jordan@buddy-app.de>, May 2021
 */
package de.felixjordan.mesibo.client.backend;

import de.felixjordan.mesibo.client.MesiboApiException;

/**
 * @author Felix Jordan
 */
public class MesiboErrorResponseException extends MesiboApiException {

	private final BaseOperationResult result;

	/**
	 * @param errorResult the error result causing the exception
	 */
	public MesiboErrorResponseException(BaseOperationResult errorResult) {
		super("Mesibo error result: " + errorResult.getError());
		if (!errorResult.isError()) {
			throw new IllegalArgumentException("errorResult must represent an error (error property not null)");
		}
		this.result = errorResult;
	}

	/**
	 * Returns the error code returned by Mesibo.
	 * 
	 * @return
	 */
	public String getError() {
		return result.getError();
	}
}
