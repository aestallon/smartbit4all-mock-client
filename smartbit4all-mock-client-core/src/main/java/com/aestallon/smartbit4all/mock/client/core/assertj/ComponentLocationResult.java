package com.aestallon.smartbit4all.mock.client.core.assertj;

import com.aestallon.smartbit4all.mock.client.core.state.ClientComponent;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public sealed interface ComponentLocationResult<T extends ClientComponent> {

  // TODO: Make Specifier a separate type...


  record Some<T extends ClientComponent>(T component, String specifier)
      implements ComponentLocationResult<T> {}


  record None<T extends ClientComponent>(String specifier, String reason)
      implements ComponentLocationResult<T> {}

  static <T extends ClientComponent> ComponentLocationResult<T> some(T component,
                                                                     String specifier) {
    return new Some<>(component, specifier);
  }

  static ComponentLocationResult<ClientView> some(ClientView view) {
    return some(view, view.toString());
  }

  static <T extends ClientComponent> ComponentLocationResult<T> none(String specifier,
                                                                     String reason) {
    return new None<>(specifier, reason);
  }

}
