package com.aestallon.smartbit4all.mock.client.core.state.component.layout;

import com.aestallon.smartbit4all.mock.client.core.api.newtype.WidgetId;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public final class Form extends AbstractWidget<Form, Form.Key> {

  public sealed interface Key extends WidgetKey<Form> {

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


  public Form(ClientView view, Key widgetKey) {
    super(view, widgetKey);
  }

  public Form(ClientView view) {
    super(view, new Key.None());
  }



}
