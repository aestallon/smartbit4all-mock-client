package com.aestallon.smartbit4all.mock.client.core.state.component;

import com.aestallon.smartbit4all.mock.client.core.MockClient;
import com.aestallon.smartbit4all.mock.client.core.state.ClientComponent;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public class ViewComponent extends ClientComponent {
  
  protected final ClientView view;
  
  protected ViewComponent(MockClient client, ClientView view) {
    super(client);
    this.view = view;
  }
  
}
