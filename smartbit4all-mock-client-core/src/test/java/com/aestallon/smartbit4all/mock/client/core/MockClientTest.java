package com.aestallon.smartbit4all.mock.client.core;

import java.net.http.HttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.aestallon.smartbit4all.mock.client.core.client.MockClient;

class MockClientTest {
  
  private static final String USERNAME = "redacted";
  private static final String PASSWORD = USERNAME;
  @Test
  void foo() {
    final WebTestClient httpClient = WebTestClient.bindToServer()
        .clientConnector(new JdkClientHttpConnector(HttpClient.newHttpClient()))
        .baseUrl("https://demo2.it4all-cloud.com:9440")
        .build();
    final var client = MockClient.remote(httpClient)
        .withDefaultInitialization()
        .withLocalAuthentication(USERNAME, PASSWORD)
        .withLaunchCall("/home/start")
        .build();
    client.view("Home").button("Törzsadatok").click();
    client.api();
  }
  
  

}