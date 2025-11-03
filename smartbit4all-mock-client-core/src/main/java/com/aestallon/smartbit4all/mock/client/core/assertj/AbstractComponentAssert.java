package com.aestallon.smartbit4all.mock.client.core.assertj;

import java.util.function.Consumer;
import org.assertj.core.api.Assertions;
import com.aestallon.smartbit4all.mock.client.core.state.ClientComponent;

public abstract class AbstractComponentAssert<SELF extends AbstractComponentAssert<SELF, T>, T extends ClientComponent> {

  protected final ComponentLocationResult<T> result;

  protected AbstractComponentAssert(ComponentLocator<T> locator) {
    this.result = locator.get();
  }

  protected abstract SELF self();

  public final SELF isPresent() {
    return checkOnPresentOrFail(it -> {}, "", " is not present ");
  }
  
  protected SELF checkOnPresentOrFail(Consumer<T> consumer, String missingPrefix) {
    return checkOnPresentOrFail(consumer, missingPrefix, " ");
  }
  
  protected SELF checkOnPresentOrFail(Consumer<T> consumer, String missingPrefix, String missingSuffix) {
    switch (result) {
      case ComponentLocationResult.Some<T> s -> consumer.accept(s.component());
      case ComponentLocationResult.None(String specifier, String reason) ->
          Assertions.fail(missingPrefix + specifier + missingSuffix + "because " + reason);
    }
    
    return self();
  }

}
