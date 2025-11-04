package com.aestallon.smartbit4all.mock.client.core.api.impl;

import org.springframework.test.web.reactive.server.WebTestClient;

public final class CustomAPI extends AbstractAPI {

  public CustomAPI(WebTestClient client, RequestContext requestContext) {
    super(client, requestContext);
  }

  public void get(String endpoint) {
    get(RequestSpec.of(UriSpec.of(endpoint), Void.class));
  }

}
