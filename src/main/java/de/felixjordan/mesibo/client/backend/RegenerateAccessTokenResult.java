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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Felix Jordan
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) // deserialization
@Getter
public class RegenerateAccessTokenResult extends BaseOperationResult {

	private User user;

	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PRIVATE) // deserialization
	@Getter
	public static class User {

		private String uid;
		private String appid;
		private String token;
	}
}
