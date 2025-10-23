package com.aestallon.smartbit4all.mock.client.core;

public class MockClient {
  
  /*
   * 
   * how usage should look like:
   * 
   * MockClient client = MockClient
   *   .local(webApplicationContext)
   *   .withDefaultInitialization()
   *   .withLaunchCall(ctx -> ctx.get("/api/v1/launch"))
   *   .build();
   * 
   * final var homeView = client.view("Home);
   * assertThat(homeView).isPresent();
   * 
   * final var profileBtn = client.view("Home").button("Profile");
   * assertThat(profileBtn).isPresent().isEnabled();
   * 
   * profileBtn.click();
   * final var profileView = client.view("Profile");
   * assertThat(profileView)
   *   .isPresent().isValid()
   *   .hasFieldsInAnyOrder("username", "email")
   *   .hasButton("Save");
   * 
   * profileView.field("email").setValue("");
   * assertThat(profileView).isInvalid();
   * 
   * profileView.button("Save").click();
   * assertThat(profileView).isDisplayingErrorMessage();
   *  
   */
  
  
  
}
