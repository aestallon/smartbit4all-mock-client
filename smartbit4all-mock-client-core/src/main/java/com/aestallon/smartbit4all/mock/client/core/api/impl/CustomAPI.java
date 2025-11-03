package com.aestallon.smartbit4all.mock.client.core.api.impl;

import org.springframework.test.web.reactive.server.WebTestClient;
import com.aestallon.smartbit4all.mock.client.core.client.InteractionContext;

public final class CustomAPI extends AbstractAPI {

  public CustomAPI(WebTestClient client,
                   RequestContext requestContext,
                   InteractionContext interactionContext) {
    super(client, requestContext, interactionContext);
  }

  public void get(String endpoint) {
    get(RequestSpec.of(UriSpec.of(endpoint), Void.class));
  }

}
