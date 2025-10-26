package com.aestallon.smartbit4all.mock.client.core.client;

import java.util.Objects;
import java.util.function.Consumer;
import org.assertj.core.util.Strings;
import org.smartbit4all.api.localauthentication.bean.LocalAuthenticationLoginRequest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewContextId;

public final class MockClientBuilder {
  private static final String BASE_PATH_DEFAULT = "/api";

  private final WebTestClient httpClient;

  private String viewApiBasePath = BASE_PATH_DEFAULT;
  private String gridApiBasePath = BASE_PATH_DEFAULT;
  private String localAuthBasePath = BASE_PATH_DEFAULT;
  private String sessionBasePath = BASE_PATH_DEFAULT;

  private Consumer<MockClient> authenticator;
  private String launchEndpoint;

  private String token;
  private ViewContextId viewContextId;

  private boolean defaultInitialization;

  MockClientBuilder(WebApplicationContext applicationContext) {
    this(WebTestClient.bindToApplicationContext(applicationContext).configureClient().build());
  }

  MockClientBuilder(WebTestClient httpClient) {
    this.httpClient = Objects.requireNonNull(httpClient);
  }

  public MockClientBuilder withLocalAuthentication(String username, String password) {
    authenticator = client -> client.api()
        .localAuth()
        .login(new LocalAuthenticationLoginRequest()
            .username(username)
            .password(password));
    return this;
  }

  public MockClientBuilder withDefaultInitialization() {
    this.defaultInitialization = true;
    return this;
  }

  public MockClientBuilder withLaunchCall(String endpoint) {
    this.launchEndpoint = endpoint;
    return this;
  }

  public MockClientBuilder withToken(String token) {
    this.token = token;
    return this;
  }

  public MockClientBuilder withViewContext(ViewContextId viewContextId) {
    this.viewContextId = viewContextId;
    return this;
  }

  public MockClientBuilder viewApiBasePath(String basePath) {
    this.viewApiBasePath = basePath;
    return this;
  }

  public MockClientBuilder gridApiBasePath(String basePath) {
    this.gridApiBasePath = basePath;
    return this;
  }

  public MockClientBuilder localAuthBasePath(String basePath) {
    this.localAuthBasePath = basePath;
    return this;
  }

  public MockClientBuilder sessionBasePath(String basePath) {
    this.sessionBasePath = basePath;
    return this;
  }

  public MockClient build() {

    final var actor = new MockClient(httpClient);
    if (defaultInitialization) {
      actor.startSession();

      if (authenticator != null) {
        authenticator.accept(actor);
        actor.getSession();
      }

      actor.startViewContext();
    }

    if (!Strings.isNullOrEmpty(launchEndpoint)) {
      actor.api().custom().get(launchEndpoint);
    }

    actor.syncViewContext();
    return actor;
  }
}
