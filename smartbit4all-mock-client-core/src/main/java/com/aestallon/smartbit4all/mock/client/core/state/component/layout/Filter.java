package com.aestallon.smartbit4all.mock.client.core.state.component.layout;

import com.aestallon.smartbit4all.mock.client.core.api.newtype.WidgetId;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public final class Filter extends DeferredInitWidget<Filter, Filter.Key> {


  public sealed interface Key extends WidgetKey<Filter.Key, Filter> {
    record Custom(String strVal) implements Key {
      @Override
      public WidgetId asId() {
        return new WidgetId(strVal);
      }
    }


    record None() implements Key {
      @Override
      public WidgetId asId() {
        return null;
      }
    }
  }

  public Filter(ClientView view, Key.Custom widgetKey) {
    super(view, widgetKey);
  }

  public Filter(ClientView view) {
    super(view, new Key.None());
  }

  @Override
  public void init() {
    
  }
}
