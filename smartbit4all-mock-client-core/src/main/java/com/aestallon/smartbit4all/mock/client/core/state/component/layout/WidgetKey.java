package com.aestallon.smartbit4all.mock.client.core.state.component.layout;

import com.aestallon.smartbit4all.mock.client.core.api.newtype.WidgetId;

public sealed interface WidgetKey<K extends WidgetKey<K, W>, W extends AbstractWidget<W, K>>
    permits CompositeLayout.Key, Filter.Key, Form.Key, Grid.Key, Toolbar.Key {

  WidgetId asId();

}


