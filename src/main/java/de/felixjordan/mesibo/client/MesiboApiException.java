/* 
 * Copyright (c) BUDDY Activities UG (haftungsbeschränkt) - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential
 *
 * Written by Felix Jordan <felix.jordan@buddy-app.de>, May 2021
 */
package de.felixjordan.mesibo.client;

/**
 * @author Felix Jordan
 */
public class MesiboApiException extends Exception {

	public MesiboApiException(String message) {
		super(message);
	}

	public MesiboApiException(String message, Exception exception) {
		super(message, exception);
	}
}
