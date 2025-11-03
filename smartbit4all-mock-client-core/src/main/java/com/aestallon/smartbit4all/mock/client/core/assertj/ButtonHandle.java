package com.aestallon.smartbit4all.mock.client.core.assertj;

import org.assertj.core.api.Fail;
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
    switch (componentLocator.get()) {
      case ComponentLocationResult.Some(Button button, String specifier) -> button.click();
      case ComponentLocationResult.None(String specifier, String reason) -> Fail.fail(
          "Could not click button " + specifier + " because " + reason);
    }
  }

  public UiAction uiAction() {
    return extract("uiAction", Button::action);
  }

  public UiActionDescriptor uiActionDescriptor() {
    return extract("uiActionDescriptor", Button::descriptor);
  }

}
