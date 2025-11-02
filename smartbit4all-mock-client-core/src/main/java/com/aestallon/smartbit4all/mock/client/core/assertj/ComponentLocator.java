package com.aestallon.smartbit4all.mock.client.core.assertj;

import java.util.function.Supplier;
import com.aestallon.smartbit4all.mock.client.core.state.ClientComponent;

@FunctionalInterface
public interface ComponentLocator<T extends ClientComponent>
    extends Supplier<ComponentLocationResult<T>> {
}
