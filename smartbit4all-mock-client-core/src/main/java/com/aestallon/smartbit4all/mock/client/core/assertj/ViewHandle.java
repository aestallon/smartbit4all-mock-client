package com.aestallon.smartbit4all.mock.client.core.assertj;

import org.assertj.core.api.Assertions;
import com.aestallon.smartbit4all.mock.client.core.client.MockClient;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public class ViewHandle extends ComponentHandle<ViewHandle, ClientView> {

  public ViewHandle(ComponentLocator<ClientView> componentLocator) {
    super(componentLocator);
  }

  @Override
  protected ViewHandle self() {
    return this;
  }

  public ButtonHandle buttonLabelled(String label) {
    return new ButtonHandle(() -> switch (componentLocator.get()) {
      case ComponentLocationResult.Some(ClientView view) -> view.button(label)
          .map(ComponentLocationResult::some)
          .orElseGet(() -> ComponentLocationResult.none("", ""));
      case ComponentLocationResult.None(String specifier, String reason) ->
          ComponentLocationResult.none(specifier + " --> Button[ " + "label: " + label + " ]",
              reason);
    });
  }

  public <T> ViewModelHandle<T> model(Class<T> modelType) {
    switch (componentLocator.get()) {
      case ComponentLocationResult.Some(ClientView view) -> {
        final Object data = view.componentModel().getData();
        final T t = MockClient.coerceType(data, modelType);
        return new ViewModelHandle<>(t);
      }
      case ComponentLocationResult.None(String specifier, String reason) -> {
        Assertions.fail("Could not retrieve model of " + specifier + " because " + reason);
        return new ViewModelHandle<>(null);
      }
    }
  }
  
  

}
