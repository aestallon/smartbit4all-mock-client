package com.aestallon.smartbit4all.mock.client.core.state.component;

import com.aestallon.smartbit4all.mock.client.core.state.ClientComponent;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public class ViewComponent extends ClientComponent {

  protected final ClientView view;

  protected ViewComponent(ClientView view) {
    super(view.client());
    this.view = view;
  }

}
