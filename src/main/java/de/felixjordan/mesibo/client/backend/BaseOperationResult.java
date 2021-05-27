/* 
 * Copyright (c) BUDDY Activities UG (haftungsbeschränkt) - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential
 *
 * Written by Felix Jordan <felix.jordan@buddy-app.de>, May 2021
 */
package de.felixjordan.mesibo.client.backend;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Base class for operation results. Represents common properties returned by
 * the Mesibo API.
 * 
 * @author Felix Jordan
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED) // deserialization
@Getter
public abstract class BaseOperationResult {

	/**
	 * Whether there is a result or not.
	 */
	private boolean result;

	/**
	 * An error, or <code>null</code> if the request was successful.
	 */
	private String error;

	/**
	 * Whether the result represents an error. If true, check {@link #getError()}
	 * for an error code.
	 * 
	 * @return
	 */
	public boolean isError() {
		return error != null;
	}
}
