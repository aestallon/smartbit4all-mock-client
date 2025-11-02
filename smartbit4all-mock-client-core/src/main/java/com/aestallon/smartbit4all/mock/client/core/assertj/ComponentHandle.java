package com.aestallon.smartbit4all.mock.client.core.assertj;

import java.util.Objects;
import org.assertj.core.api.AssertProvider;
import org.assertj.core.api.Assertions;
import com.aestallon.smartbit4all.mock.client.core.state.ClientComponent;

public abstract class ComponentHandle<SELF extends ComponentHandle<SELF, T>, T extends ClientComponent>
    implements AssertProvider<SELF> {

  /**
   * The wrapped component of the client.
   *
   * <p>
   * Every handle either wraps a component (if this component is present in the client's state) or
   * NULL.
   *
   * <p>
   * Interactions and positive assertions about NULL components should generally fail.
   */
  protected final ComponentLocator<T> componentLocator;

  protected ComponentHandle(ComponentLocator<T> componentLocator) {
    this.componentLocator = Objects.requireNonNull(
        componentLocator,
        "Component Locator must not be null");
  }

  public SELF isPresent() {
    switch (componentLocator.get()) {
      case ComponentLocationResult.Some<T> s -> { /* no need to do anything */}
      case ComponentLocationResult.None(String specifier, String reason) ->
          Assertions.fail(specifier + " is not present because " + reason);
    }

    return self();
  }

  protected abstract SELF self();

  @Override
  public SELF assertThat() {
    return self();
  }
}
