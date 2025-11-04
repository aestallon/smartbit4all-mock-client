package com.aestallon.smartbit4all.mock.client.core;

import java.net.http.HttpClient;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.aestallon.smartbit4all.mock.client.core.assertj.ButtonHandle;
import com.aestallon.smartbit4all.mock.client.core.client.MockClient;

class MockClientTest {

  private static final String USERNAME = "admin";
  private static final String PASSWORD = USERNAME;

  @Test
  void foo() {
    // client setup:
    final WebTestClient httpClient = WebTestClient.bindToServer()
        .clientConnector(new JdkClientHttpConnector(HttpClient.newHttpClient()))
        .baseUrl("https://demo2.it4all-cloud.com:9440")
        .build();
    final var client = MockClient.remote(httpClient)
        .withDefaultInitialization()
        .withLocalAuthentication(USERNAME, PASSWORD)
        .withLaunchCall("/home/start")
        .build();
    
    // a component handle which is reusable throughout the entire test:
    ButtonHandle newDossierBtn = client
        .view("MainContainer")
        .buttonLabelled("Új dosszié");
    assertThat(newDossierBtn)
        .isEnabled()   // <-- custom assertion provider with typesafe methods
        .hasLabel("Új dosszié")
        .hasCode("NEW_DOCUMENT");
    client.view("MainContainer")
        .assertThatModel(Object.class)
        .isNotNull();  // <- convenience assertion providers for page model / ComponentModel
    newDossierBtn.click();
    // can request handles even if the component does not exist (yet)
    ButtonHandle fooBtn = client.view("MainContainer").toolbar("controls").buttonLabelled("foo");
    fooBtn.click();
    assertThat(newDossierBtn).isPresent();
  }



}