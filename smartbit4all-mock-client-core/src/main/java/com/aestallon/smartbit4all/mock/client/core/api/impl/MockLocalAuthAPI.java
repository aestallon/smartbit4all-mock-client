package com.aestallon.smartbit4all.mock.client.core.api.impl;

import org.smartbit4all.api.localauthentication.bean.LocalAuthenticationLoginRequest;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.aestallon.smartbit4all.mock.client.core.api.LocalAuthAPI;
import com.aestallon.smartbit4all.mock.client.core.client.InteractionContext;

/**
 * WebTestClient-backed implementation of LocalAuthAPI.
 */
public class MockLocalAuthAPI extends AbstractAPI implements LocalAuthAPI {

  public MockLocalAuthAPI(WebTestClient client, RequestContext context,
                          InteractionContext interactionContext) {
    super(client, context, interactionContext);
  }

  @Override
  public void login(LocalAuthenticationLoginRequest request) {
    post(RequestSpec.of(UriSpec.of("/login"), Void.class, request));
  }

  @Override
  public void logout() {
    post(RequestSpec.of(UriSpec.of("/logout")));
  }
}
