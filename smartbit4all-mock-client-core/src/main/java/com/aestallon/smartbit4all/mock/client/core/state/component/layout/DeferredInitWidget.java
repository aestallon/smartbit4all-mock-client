package com.aestallon.smartbit4all.mock.client.core.state.component.layout;

import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public abstract sealed class DeferredInitWidget<T extends AbstractWidget<T, K>, K extends WidgetKey<T>>
    extends AbstractWidget<T, K>
    permits Grid, Filter {

  protected DeferredInitWidget(ClientView view,
                               K widgetKey) {
    super(view, widgetKey);
  }
  
  public abstract void init();

}
