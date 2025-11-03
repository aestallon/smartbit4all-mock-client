package com.aestallon.smartbit4all.mock.client.core.state.component.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.smartbit4all.api.view.bean.UiAction;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.WidgetId;
import com.aestallon.smartbit4all.mock.client.core.state.component.interactable.Button;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;
import com.google.common.base.Strings;

public final class Toolbar extends AbstractWidget<Toolbar, Toolbar.Key> {

  public sealed interface Key extends WidgetKey<Toolbar> {

    static Toolbar.Key fromString(String strVal) {
      return Strings.isNullOrEmpty(strVal) ? Default.INSTANCE : new Toolbar.Key.Custom(strVal);
    }

    record Custom(String strVal) implements Key {

      @Override
      public WidgetId asId() {
        return new WidgetId(strVal);
      }
    }


    record Default() implements Key {
      public static final Key.Default INSTANCE = new Key.Default();

      @Override
      public WidgetId asId() {
        return null;
      }

    }

  }


  private final List<Button> buttons;

  public Toolbar(ClientView view) {
    this(view, Key.Default.INSTANCE);
  }

  public Toolbar(ClientView view, Key id) {
    super(view, id);
    this.buttons = new ArrayList<>();
  }

  public void add(UiAction action) {
    buttons.add(new Button(this, action));
  }

  public List<Button> buttons() {
    return Collections.unmodifiableList(buttons);
  }

  public void buttons(List<Button> buttons) {
    this.buttons.clear();
    this.buttons.addAll(buttons);
  }

  public Optional<Button> button(final String label) {
    return buttons.stream().filter(it -> label.equals(it.label())).findFirst();
  }

  @Override
  public String toString() {
    if (id() == Key.Default.INSTANCE) {
      return "Toolbar DEFAULT";
    } else {
      return "Toolbar [ " + id().asId() +" ]";
    }
  }
}
