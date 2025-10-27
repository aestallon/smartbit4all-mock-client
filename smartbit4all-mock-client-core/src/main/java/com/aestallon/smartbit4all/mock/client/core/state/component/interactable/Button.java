package com.aestallon.smartbit4all.mock.client.core.state.component.interactable;

import org.smartbit4all.api.view.bean.UiAction;
import org.smartbit4all.api.view.bean.UiActionRequest;
import com.aestallon.smartbit4all.mock.client.core.client.MockClient;
import com.aestallon.smartbit4all.mock.client.core.state.component.layout.Toolbar;

public final class Button {

  private final Toolbar toolbar;
  private final MockClient client;
  private final UiAction action;

  public Button(Toolbar toolbar, UiAction action) {
    this.toolbar = toolbar;
    this.client = toolbar.client();
    this.action = action;
  }

  public String code() {
    return action.getCode();
  }

  public String label() {
    return action.getDescriptor().getTitle();
  }

  public void click() {
    final var request = new UiActionRequest()
        .code(action.getCode())
        .identifier(action.getIdentifier())
        .path(action.getPath());
    if (Boolean.TRUE.equals(action.getModel())) {
      request.putParamsItem("model", toolbar.view().componentModel().getData());
    }

    final var change = client
        .api().component()
        .performAction(toolbar.view().id(), request);
    client.onViewContextChange(change);
  }

}
