package com.aestallon.smartbit4all.mock.client.core.state.component.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartbit4all.api.formdefinition.bean.SmartLayoutDefinition;
import org.smartbit4all.api.smartcomponentlayoutdefinition.bean.SmartComponentLayoutDefinition;
import org.smartbit4all.api.view.bean.UiAction;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.WidgetId;
import com.aestallon.smartbit4all.mock.client.core.state.component.interactable.Button;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public final class CompositeLayout extends AbstractWidget<CompositeLayout, CompositeLayout.Key> {

  private static final Logger log = LoggerFactory.getLogger(CompositeLayout.class);


  public enum Orientation { HORIZONTAL, VERTICAL }


  public sealed interface Key extends WidgetKey<CompositeLayout.Key, CompositeLayout> {

    record Custom(String strVal) implements CompositeLayout.Key {
      @Override
      public WidgetId asId() {
        return new WidgetId(strVal);
      }
    }


    record None() implements CompositeLayout.Key {
      @Override
      public WidgetId asId() {
        return null;
      }
    }
  }


  private final List<AbstractWidget<?, ?>> children;

  public CompositeLayout(ClientView view) {
    this(view, new Key.None());
  }

  public CompositeLayout(ClientView view, Orientation orientation) {
    this(view, orientation, new Key.None());
  }

  public CompositeLayout(ClientView view, CompositeLayout.Key widgetKey) {
    this(view, Orientation.VERTICAL, widgetKey);
  }

  public CompositeLayout(ClientView view, Orientation orientation, CompositeLayout.Key widgetKey) {
    super(view, widgetKey);
    this.children = new ArrayList<>();
  }

  public void add(AbstractWidget<?, ?> widget) {
    children.add(widget);
  }


  public Form add(SmartLayoutDefinition formDef) {
    return add(new Form.Key.None(), formDef);
  }

  public Form add(Form.Key key, SmartLayoutDefinition formDef) {
    final var form = new Form(view, key);
    add(form);
    form.createWidgets(formDef);
    return form;
  }

  public List<DeferredInitWidget<?, ?>> add(SmartComponentLayoutDefinition layoutDef) {
    return add(new CompositeLayout.Key.None(), layoutDef);
  }

  public List<DeferredInitWidget<?, ?>> add(CompositeLayout.Key key,
                                            SmartComponentLayoutDefinition layoutDef) {
    return Collections.emptyList();
  }

  public void add(List<UiAction> actions) {
    final Map<Toolbar.Key, Toolbar> toolbars = getWidgets(Toolbar.class)
        .collect(Collectors.toMap(Toolbar::id, it -> it));
    for (final var action : actions) {
      final var toolbarKey = Toolbar.Key.fromString(action.getToolbar());
      final var toolbar = toolbars.get(toolbarKey);
      if (toolbar == null) {
        // TODO: Set ImplicitElementStrategy in client: FAIL, WARN, IGNORE or ASSUME
        log.warn(
            "Action {} refers to non-existent toolbar: {}. Is the toolbar implicit?", 
            action, toolbarKey);
        final var implicitToolbar = new Toolbar(view(), toolbarKey);
        add(implicitToolbar);
        implicitToolbar.add(action);
      } else {
        toolbar.add(action);
      }
    }
  }

  public Grid add(Grid.Key key) {
    return null;
  }

  private Stream<AbstractWidget<?, ?>> stream() {
    return children.stream()
        .flatMap(it -> it instanceof CompositeLayout cl ? cl.stream() : Stream.of(it));
  }

  @SuppressWarnings("unchecked")
  private <T extends AbstractWidget<T, K>, K extends WidgetKey<K, T>> T coerce(
      AbstractWidget<?, ?> widget) {
    return (T) widget;
  }

  public <T extends AbstractWidget<T, K>, K extends WidgetKey<K, T>> Optional<T> getWidget(K key) {
    return stream()
        .filter(it -> it.id().equals(key))
        .findFirst()
        .map(this::coerce);
  }

  public <T extends AbstractWidget<T, K>, K extends WidgetKey<K, T>> Stream<T> getWidgets(
      Class<T> widgetType) {
    return stream()
        .filter(widgetType::isInstance)
        .map(this::coerce);
  }

  public Optional<Button> getButton(final String code) {
    return getWidgets(Toolbar.class)
        .flatMap(it -> it.buttons().stream())
        .filter(it -> it.code().equals(code))
        .findFirst();
  }


}
