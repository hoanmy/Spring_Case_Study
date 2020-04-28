/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demo.security.config;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;

public class UserApprovalHandlerImpl extends ApprovalStoreUserApprovalHandler {

	private boolean useApprovalStore = true;

	private ClientDetailsService clientDetailsService;

	public void setClientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
		super.setClientDetailsService(clientDetailsService);
	}

	public void setUseApprovalStore(boolean useApprovalStore) {
		this.useApprovalStore = useApprovalStore;
	}

	@Override
	public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest,
			Authentication userAuthentication) {

		boolean approved = false;
		// If we are allowed to check existing approvals this will short circuit the decision
		if (useApprovalStore) {
			authorizationRequest = super.checkForPreApproval(authorizationRequest, userAuthentication);
			approved = authorizationRequest.isApproved();
		}
		else {
			if (clientDetailsService != null) {
				Collection<String> requestedScopes = authorizationRequest.getScope();
				try {
					ClientDetails client = clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
					for (String scope : requestedScopes) {
						if (client.isAutoApprove(scope)) {
							approved = true;
							break;
						}
					}
				}
				catch (ClientRegistrationException e) {
				}
			}
		}
		authorizationRequest.setApproved(approved);

		return authorizationRequest;

	}

}
