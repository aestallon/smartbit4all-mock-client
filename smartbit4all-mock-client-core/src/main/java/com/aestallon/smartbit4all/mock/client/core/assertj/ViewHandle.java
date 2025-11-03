package com.aestallon.smartbit4all.mock.client.core.assertj;

import org.smartbit4all.api.view.bean.ComponentModel;
import com.aestallon.smartbit4all.mock.client.core.client.MockClient;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public class ViewHandle extends AbstractComponentHandle<ViewHandle, ClientView, ViewAssert> {

  public ViewHandle(ComponentLocator<ClientView> componentLocator) {
    super(componentLocator, ViewAssert::new);
  }

  @Override
  protected ViewHandle self() {
    return this;
  }

  public ButtonHandle buttonLabelled(String label) {
    return new ButtonHandle(() -> switch (componentLocator.get()) {
      case ComponentLocationResult.Some(ClientView view, var specifier) -> view.button(label)
          .map(button -> ComponentLocationResult.some(button, specifier + " --> Button[ label: " + label + " ]"))
          .orElseGet(() -> ComponentLocationResult.none(
              specifier + " --> Button[ label: " + label + " ]", 
              "Button[ label: " + label + " ] is not present on the view"));
      case ComponentLocationResult.None(String specifier, String reason) ->
          ComponentLocationResult.none(
              specifier + " --> Button[ label: " + label + " ]",
              reason);
    });
  }

  public <T> T model(Class<T> modelType) {
    final Object data = extract("model", it -> it.componentModel().getData());
    return MockClient.coerceType(data, modelType);
  }
  
  public ComponentModel componentModel() {
    return extract("component model", ClientView::componentModel);
  }

}
