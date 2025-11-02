package com.aestallon.smartbit4all.mock.client.core.assertj;

import com.aestallon.smartbit4all.mock.client.core.state.ClientComponent;

public sealed interface ComponentLocationResult<T extends ClientComponent> {

  record Some<T extends ClientComponent>(T component) implements ComponentLocationResult<T> {}


  record None<T extends ClientComponent>(String specifier, String reason)
      implements ComponentLocationResult<T> {}

  static <T extends ClientComponent> ComponentLocationResult<T> some(T component) {
    return new Some<>(component);
  }

  static <T extends ClientComponent> ComponentLocationResult<T> none(String specifier,
                                                                     String reason) {
    return new None<>(specifier, reason);
  }
  
}
