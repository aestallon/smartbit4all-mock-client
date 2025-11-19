package com.aestallon.smartbit4all.mock.client.core.state.component.layout;

import com.aestallon.smartbit4all.mock.client.core.state.component.ViewComponent;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public sealed class AbstractWidget<T extends AbstractWidget<T, K>, K extends WidgetKey<K, T>>
    extends ViewComponent
    permits CompositeLayout, DeferredInitWidget, Form, Toolbar {

  protected final K widgetKey;

  protected AbstractWidget(ClientView view, K widgetKey) {
    super(view);
    this.widgetKey = widgetKey;
  }
  
  public K id() {
    return widgetKey;
  }

  public ClientView view() {
    return view;
  }
}
