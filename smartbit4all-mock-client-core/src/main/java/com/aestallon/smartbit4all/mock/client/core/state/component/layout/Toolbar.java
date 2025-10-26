package com.aestallon.smartbit4all.mock.client.core.state.component.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.WidgetId;
import com.aestallon.smartbit4all.mock.client.core.state.component.interactable.Button;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public final class Toolbar extends AbstractWidget<Toolbar, Toolbar.Key> {

  public sealed interface Key extends WidgetKey<Toolbar> {

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

  public List<Button> buttons() {
    return Collections.unmodifiableList(buttons);
  }

  public void buttons(List<Button> buttons) {
    this.buttons.clear();
    this.buttons.addAll(buttons);
  }

  public Optional<Button> button(final String code) {
    return Optional.empty();
  }

}
