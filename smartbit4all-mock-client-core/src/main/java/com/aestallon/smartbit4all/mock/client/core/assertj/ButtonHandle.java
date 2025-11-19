package com.aestallon.smartbit4all.mock.client.core.assertj;

import org.smartbit4all.api.view.bean.UiAction;
import org.smartbit4all.api.view.bean.UiActionDescriptor;
import com.aestallon.smartbit4all.mock.client.core.state.component.interactable.Button;

public class ButtonHandle extends AbstractComponentHandle<ButtonHandle, Button, ButtonAssert> {

  public ButtonHandle(ComponentLocator<Button> componentLocator) {
    super(componentLocator, ButtonAssert::new);
  }

  @Override
  protected ButtonHandle self() {
    return this;
  }

  public void click() {
    interact("click on button", Button::click);
  }

  public UiAction uiAction() {
    return extract("uiAction", Button::action);
  }

  public UiActionDescriptor uiActionDescriptor() {
    return extract("uiActionDescriptor", Button::descriptor);
  }

}
