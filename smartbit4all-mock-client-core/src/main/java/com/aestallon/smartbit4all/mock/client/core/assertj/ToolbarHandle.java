package com.aestallon.smartbit4all.mock.client.core.assertj;

import com.aestallon.smartbit4all.mock.client.core.state.component.layout.Toolbar;

public class ToolbarHandle extends AbstractComponentHandle<ToolbarHandle, Toolbar, ToolbarAssert> {

  protected ToolbarHandle(ComponentLocator<Toolbar> componentLocator) {
    super(componentLocator, ToolbarAssert::new);
  }

  @Override
  protected ToolbarHandle self() {
    return this;
  }

  public ButtonHandle buttonLabelled(String label) {
    return new ButtonHandle(() -> switch (componentLocator.get()) {
      case ComponentLocationResult.Some(Toolbar toolbar, var specifier) -> toolbar.button(label)
          .map(button -> ComponentLocationResult.some(button, specifier + " --> " + button))
          .orElseGet(() -> ComponentLocationResult.none(
              specifier + " --> Button [ label: " + label + " ]",
              "Button [ label: " + label + " ] is not present on the toolbar. Available buttons: "
              + toolbar.buttons()));
      case ComponentLocationResult.None(String specifier, String reason) ->
          ComponentLocationResult.none(
              specifier + " --> Button [ label: " + label + " ]",
              reason);
    });
  }

}
