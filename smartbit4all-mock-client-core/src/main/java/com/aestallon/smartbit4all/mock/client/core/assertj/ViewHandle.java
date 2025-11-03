package com.aestallon.smartbit4all.mock.client.core.assertj;

import org.assertj.core.api.AbstractObjectAssert;
import org.smartbit4all.api.view.bean.ComponentModel;
import com.aestallon.smartbit4all.mock.client.core.client.MockClient;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public class ViewHandle extends AbstractComponentHandle<ViewHandle, ClientView, ViewAssert> {

  public ViewHandle(ComponentLocator<ClientView> componentLocator) {
    super(componentLocator, ViewAssert::new);
  }

  @Override
  protected ViewHandle self() {
    return this;
  }

  public ButtonHandle buttonLabelled(String label) {
    return new ButtonHandle(() -> switch (componentLocator.get()) {
      case ComponentLocationResult.Some(ClientView view, var specifier) -> view.button(label)
          .map(button -> ComponentLocationResult.some(button, specifier + " --> " + button))
          .orElseGet(() -> ComponentLocationResult.none(
              specifier + " --> Button [ label: " + label + " ]",
              "Button [ label: " + label + " ] is not present on the view. Available buttons: "
              + view.buttons()));
      case ComponentLocationResult.None(String specifier, String reason) ->
          ComponentLocationResult.none(
              specifier + " --> Button [ label: " + label + " ]",
              reason);
    });
  }

  public ToolbarHandle toolbar(String identifier) {
    return new ToolbarHandle(() -> switch (componentLocator.get()) {
      case ComponentLocationResult.Some(ClientView view, var specifier) -> view.toolbar(identifier)
          .map(toolbar -> ComponentLocationResult.some(toolbar, specifier + " --> " + toolbar))
          .orElseGet(() -> ComponentLocationResult.none(
              specifier + " --> Toolbar [ " + identifier + " ]",
              "Toolbar [ " + identifier + " ] is not present on the view. Available toolbars: "
              + view.toolbars()));
      case ComponentLocationResult.None(var specifier, var reason) -> ComponentLocationResult.none(
          specifier + " --> Toolbar [ " + identifier + " ]",
          reason);
    });
  }

  public ToolbarHandle defaultToolbar() {
    return new ToolbarHandle(() -> switch (componentLocator.get()) {
      case ComponentLocationResult.Some(ClientView view, var specifier) -> {
        final var toolbar = view.toolbar();
        yield ComponentLocationResult.some(toolbar, specifier + " --> " + toolbar);
      }
      case ComponentLocationResult.None(var specifier, var reason) -> ComponentLocationResult.none(
          specifier + " --> Toolbar DEFAULT",
          reason);
    });
  }

  public static final class ViewModelAssert<T> extends AbstractObjectAssert<ViewModelAssert<T>, T> {

    public ViewModelAssert(T t) {
      super(t, ViewModelAssert.class);
    }

  }

  public <T> T model(Class<T> modelType) {
    final Object data = extract("model", it -> it.componentModel().getData());
    return MockClient.coerceType(data, modelType);
  }

  public <T> ViewModelAssert<T> assertThatModel(Class<T> modelType) {
    return new ViewModelAssert<>(model(modelType));
  }

  public static final class ComponentModelAssert
      extends AbstractObjectAssert<ComponentModelAssert, ComponentModel> {

    public ComponentModelAssert(ComponentModel componentModel) {
      super(componentModel, ComponentModelAssert.class);
    }

  }

  public ComponentModel componentModel() {
    return extract("component model", ClientView::componentModel);
  }

  public ComponentModelAssert assertThatComponentModel() {
    return new ComponentModelAssert(componentModel());
  }

}
