package com.aestallon.smartbit4all.mock.client.core.client;

import java.util.List;
import java.util.function.Consumer;
import com.aestallon.smartbit4all.mock.client.core.TestClient;

public final class ReadyTestClient {

  private final MockClient client;
  private final List<Consumer<MockClient>> actions;

  ReadyTestClient(MockClient client, List<Consumer<MockClient>> actions) {
    this.client = client;
    this.actions = actions;
  }

  public TestClient launch() {
    actions.forEach(a -> a.accept(client));
    return client;
  }

}
