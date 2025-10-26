package com.aestallon.smartbit4all.mock.client.core.state.component.layout;

import java.util.ArrayList;
import java.util.List;
import com.aestallon.smartbit4all.mock.client.core.MockClient;
import com.aestallon.smartbit4all.mock.client.core.state.component.Button;
import com.aestallon.smartbit4all.mock.client.core.state.component.ViewComponent;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public class Toolbar extends ViewComponent {

  public record Id(String strVal) {}


  private final Toolbar.Id id;
  private final List<Button> buttons;

  public Toolbar(MockClient client, ClientView view, Toolbar.Id id) {
    super(client, view);
    this.id = id;
    this.buttons = new ArrayList<>();
  }

}
