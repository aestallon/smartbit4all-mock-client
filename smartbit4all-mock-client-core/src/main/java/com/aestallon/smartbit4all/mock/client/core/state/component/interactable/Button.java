package com.aestallon.smartbit4all.mock.client.core.state.component.interactable;

import org.smartbit4all.api.view.bean.UiAction;
import org.smartbit4all.api.view.bean.UiActionDescriptor;
import org.smartbit4all.api.view.bean.UiActionRequest;
import com.aestallon.smartbit4all.mock.client.core.state.component.ViewComponent;
import com.aestallon.smartbit4all.mock.client.core.state.component.layout.Toolbar;

public final class Button extends ViewComponent {

  private final Toolbar toolbar;
  private final UiAction action;

  public Button(Toolbar toolbar, UiAction action) {
    super(toolbar.view());
    this.toolbar = toolbar;
    this.action = action;
  }

  public String code() {
    return action.getCode();
  }

  public String label() {
    final var descriptor = descriptor();
    return descriptor != null ? descriptor.getTitle() : null;
  }

  public void click() {
    final var request = new UiActionRequest()
        .code(action.getCode())
        .identifier(action.getIdentifier())
        .path(action.getPath());
    if (Boolean.TRUE.equals(action.getModel())) {
      request.putParamsItem("model", toolbar.view().componentModel().getData());
    }

    final String activity = "Clicking button: " + toolbar.view() + " --> " + toolbar + " --> " + this;
    client.performAction(activity, toolbar.view().id(), request);
  }

  public UiAction action() {
    return action;
  }

  public UiActionDescriptor descriptor() {
    return action.getDescriptor();
  }

  @Override
  public String toString() {
    return "Button [ code: " + code() + " ][ label: " + label() + " ]";
  }

}
