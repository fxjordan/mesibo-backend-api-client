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
public interface MesiboBackendApi {

	/**
	 * Adds a user (or regenerates an access token for the user) with the given ID.
	 * 
	 * @param userAddress
	 * @param appId
	 * @param expiry
	 * @param active
	 * @param name
	 * @param maxGroups
	 * @return
	 * @throws MesiboApiException
	 */
	AddUserResult addUser(
			String userAddress,
			String appId,
			Integer expiry,
			Boolean active,
			String name,
			Integer maxGroups) throws MesiboApiException;

	/**
	 * Adds a user (or regenerates an access token for the user) with the given ID.
	 * 
	 * @param userAddress
	 * @param appId
	 * @return
	 * @throws MesiboApiException
	 */
	AddUserResult addUser(
			String userAddress,
			String appId) throws MesiboApiException;
}
