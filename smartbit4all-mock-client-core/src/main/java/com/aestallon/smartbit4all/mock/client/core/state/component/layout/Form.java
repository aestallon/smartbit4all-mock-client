package com.aestallon.smartbit4all.mock.client.core.state.component.layout;

import java.util.ArrayList;
import java.util.List;
import org.smartbit4all.api.formdefinition.bean.SmartLayoutDefinition;
import org.smartbit4all.api.formdefinition.bean.ValueChangeMode;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.WidgetId;
import com.aestallon.smartbit4all.mock.client.core.state.component.interactable.form.FormElement;
import com.aestallon.smartbit4all.mock.client.core.state.component.interactable.form.InputField;
import com.aestallon.smartbit4all.mock.client.core.state.component.interactable.form.Select;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public final class Form extends AbstractWidget<Form, Form.Key> {

  public sealed interface Key extends WidgetKey<Form.Key, Form> {

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

  private final List<FormElement> elements;

  public Form(ClientView view, Key widgetKey) {
    super(view, widgetKey);
    elements = new ArrayList<>();
  }

  public Form(ClientView view) {
    this(view, new Key.None());
  }
  
  public void createWidgets(SmartLayoutDefinition layoutDef) {
    for (final var e : layoutDef.getWidgets()) {
      final boolean immediateAction = ValueChangeMode.IMMEDIATE_ACTION == e.getValueChangeMode();
      final var dataPath =  e.getKey().split("\\.");
      switch (e.getType()) {
        case SELECT -> elements.add(new Select(this, dataPath, immediateAction));
        case TEXT_FIELD -> elements.add(new InputField(this, dataPath, immediateAction));
      }
    }
  }



}
