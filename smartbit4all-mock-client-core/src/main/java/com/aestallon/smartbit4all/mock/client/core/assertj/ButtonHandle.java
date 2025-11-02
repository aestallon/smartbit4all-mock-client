package com.aestallon.smartbit4all.mock.client.core.assertj;

import org.assertj.core.api.Fail;
import com.aestallon.smartbit4all.mock.client.core.state.component.interactable.Button;

public class ButtonHandle extends ComponentHandle<ButtonHandle, Button> {

  public ButtonHandle(ComponentLocator<Button> componentLocator) {
    super(componentLocator);
  }

  @Override
  protected ButtonHandle self() {
    return this;
  }

  public void click() {
    switch (componentLocator.get()) {
      case ComponentLocationResult.Some(Button button) -> button.click();
      case ComponentLocationResult.None(String specifier, String reason) -> Fail.fail(
          "Could not click button " + specifier + " because " + reason);
    }
  }

}
