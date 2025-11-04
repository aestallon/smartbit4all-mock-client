package com.aestallon.smartbit4all.mock.client.core.client;

import com.aestallon.smartbit4all.mock.client.core.api.ComponentAPI;
import com.aestallon.smartbit4all.mock.client.core.api.GridAPI;
import com.aestallon.smartbit4all.mock.client.core.api.LocalAuthAPI;
import com.aestallon.smartbit4all.mock.client.core.api.SessionAPI;
import com.aestallon.smartbit4all.mock.client.core.api.impl.CustomAPI;
import com.aestallon.smartbit4all.mock.client.core.api.impl.MockComponentAPI;
import com.aestallon.smartbit4all.mock.client.core.api.impl.MockGridAPI;
import com.aestallon.smartbit4all.mock.client.core.api.impl.MockLocalAuthAPI;
import com.aestallon.smartbit4all.mock.client.core.api.impl.MockSessionAPI;

public record APIFactory(MockClient client) {

  public ComponentAPI component(InteractionContext interactionContext) {
    return new MockComponentAPI(client.webClient(), client.requestContext(interactionContext));
  }

  public SessionAPI session(InteractionContext interactionContext) {
    return new MockSessionAPI(client.webClient(), client.requestContext(interactionContext));
  }

  public LocalAuthAPI localAuth(InteractionContext interactionContext) {
    return new MockLocalAuthAPI(client.webClient(), client.requestContext(interactionContext));
  }

  public GridAPI grid(InteractionContext interactionContext) {
    return new MockGridAPI(client.webClient(), client.requestContext(interactionContext));
  }

  public CustomAPI custom(InteractionContext interactionContext) {
    return new CustomAPI(client.webClient(), client.requestContext(interactionContext));
  }

}
