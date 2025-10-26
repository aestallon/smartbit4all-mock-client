package com.aestallon.smartbit4all.mock.client.core.state;

import com.aestallon.smartbit4all.mock.client.core.MockClient;

public abstract class ClientComponent {

  protected final MockClient client;

  protected ClientComponent(MockClient client) {
    this.client = client;
  }

  public MockClient client() { return client; }

}
