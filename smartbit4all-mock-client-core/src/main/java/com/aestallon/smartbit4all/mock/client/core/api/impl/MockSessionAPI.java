package com.aestallon.smartbit4all.mock.client.core.api.impl;

import org.smartbit4all.api.session.bean.GetAuthenticationProvidersResponse;
import org.smartbit4all.api.session.bean.RefreshSessionRequest;
import org.smartbit4all.api.session.bean.SessionInfoData;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.aestallon.smartbit4all.mock.client.core.api.SessionAPI;

/**
 * WebTestClient-backed implementation of SessionAPI.
 */
public class MockSessionAPI extends AbstractAPI implements SessionAPI {

  public MockSessionAPI(WebTestClient client, RequestContext context) {
    super(client, context);
  }

  @Override
  public SessionInfoData startSession() {
    return put(RequestSpec.of(UriSpec.of("/session"), SessionInfoData.class));
  }

  @Override
  public SessionInfoData getSession() {
    return get(RequestSpec.of(UriSpec.of("/session"), SessionInfoData.class));
  }

  @Override
  public GetAuthenticationProvidersResponse getAuthenticationProviders() {
    return get(RequestSpec.of(UriSpec.of("/session/authentication/providers"), GetAuthenticationProvidersResponse.class));
  }

  @Override
  public SessionInfoData refreshSession(RefreshSessionRequest refreshSessionRequest) {
    return post(RequestSpec.of(UriSpec.of("/session/refresh"), SessionInfoData.class, refreshSessionRequest));
  }

  @Override
  public SessionInfoData setLocale(String locale) {
    return put(RequestSpec.of(UriSpec.of("/session/locale/{locale}", locale), SessionInfoData.class));
  }
}
