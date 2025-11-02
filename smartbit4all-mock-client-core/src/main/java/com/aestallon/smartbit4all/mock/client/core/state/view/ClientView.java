package com.aestallon.smartbit4all.mock.client.core.state.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.Fail;
import org.smartbit4all.api.view.bean.ComponentModel;
import org.smartbit4all.api.view.bean.ViewContextChange;
import org.smartbit4all.api.view.bean.ViewData;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.WidgetId;
import com.aestallon.smartbit4all.mock.client.core.client.MockClient;
import com.aestallon.smartbit4all.mock.client.core.state.ClientComponent;
import com.aestallon.smartbit4all.mock.client.core.state.component.interactable.Button;
import com.aestallon.smartbit4all.mock.client.core.state.component.layout.AbstractWidget;
import com.aestallon.smartbit4all.mock.client.core.state.component.layout.CompositeLayout;
import com.aestallon.smartbit4all.mock.client.core.state.component.layout.DeferredInitWidget;
import com.aestallon.smartbit4all.mock.client.core.state.component.layout.Form;
import com.aestallon.smartbit4all.mock.client.core.state.component.layout.Toolbar;
import com.aestallon.smartbit4all.mock.client.core.state.component.layout.WidgetKey;
import com.aestallon.smartbit4all.mock.client.core.util.StringUtil;

public abstract sealed class ClientView
    extends ClientComponent
    permits ClientDialogView, ClientNormalView {

  protected ViewId id;
  protected String name;
  protected ClientView parent;
  protected final List<ClientDialogView> dialogs = new ArrayList<>();

  protected ComponentModel model;

  protected final CompositeLayout rootLayout;

  protected ClientView(MockClient client, ViewId id, String name) {
    this(client, id, name, null);
  }

  protected ClientView(MockClient client, ViewId id, String name, ClientView parent) {
    super(client);
    this.id = id;
    this.name = name;
    this.parent = parent;

    rootLayout = new CompositeLayout(this);
    rootLayout.add(new Toolbar(this));
  }

  public ViewId id() {
    return id;
  }

  public void id(ViewId id) {
    this.id = id;
  }

  public String name() {
    return name;
  }

  public Optional<ClientView> parent() {
    return Optional.ofNullable(parent);
  }

  public void componentModel(ComponentModel componentModel) {
    model = componentModel;
    model.getLayouts().forEach((formId, formDef) -> {
      rootLayout.add(new Form.Key.Custom(formId), formDef);
    });

    final List<DeferredInitWidget<?, ?>> deferredInitWidgets = new ArrayList<>();
    model.getComponentLayouts().forEach((layoutId, layoutDef) -> {
      deferredInitWidgets.addAll(rootLayout.add(
          new CompositeLayout.Key.Custom(layoutId),
          layoutDef));
    });
    rootLayout.add(model.getActions());

    deferredInitWidgets.forEach(DeferredInitWidget::init);
  }

  public ComponentModel componentModel() {
    return model;
  }

  public void setData(String[] dataPath, Object o) {
    if (dataPath == null || dataPath.length < 1) {
      // TODO: Throw appropriate ClientException
      Fail.fail("Cannot set value " + o + " on null or empty data path!");
      return;
    }

    Map<String, Object> map;
    if (model.getData() instanceof Map<?, ?> m) {
      @SuppressWarnings("unchecked")
      final var temp = (Map<String, Object>) m;
      map = temp;
    } else {
      map = new LinkedHashMap<>();
      model.setData(map);
    }

    for (int i = 0; i < dataPath.length - 1; i++) {
      map = ensureNestedValue(map, dataPath[i]);
    }
    map.put(dataPath[dataPath.length - 1], o);
  }


  @SuppressWarnings("unchecked")
  private Map<String, Object> ensureNestedValue(Map<String, Object> m, String k) {
    Object v = m.computeIfAbsent(k, k1 -> new LinkedHashMap<String, Object>());
    return (Map<String, Object>) v;
  }

  public void ensureLoaded() {
    if (model == null) {
      load();
    }
  }

  public void load() {
    ViewContextChange change = client.api().component().getComponentModel2(id);
    client.onViewContextChange(change);
  }

  public void close() {
    parent().ifPresent(it -> it.drop(this));
  }

  public ClientDialogView openDialog(ViewData viewData) {
    final var dialogId = new ViewId(viewData.getUuid());
    final var name = viewData.getViewName();
    final var parentId = new ViewId(viewData.getContainerUuid());
    // TODO: Throw appropriate ClientException
    assertThat(parentId).isEqualTo(id);

    final var dialog = new ClientDialogView(client, dialogId, name, this);
    dialogs.add(dialog);
    return dialog;
  }

  public void drop(ClientView view) {
    if (view instanceof ClientDialogView dialog) {
      dialogs.remove(dialog);
    }
  }

  public void onWidgetChanges(Collection<WidgetId> changedWidgets) {

  }

  protected <T extends AbstractWidget<T, K>, K extends WidgetKey<T>> Optional<T> getWidget(K key) {
    return rootLayout.getWidget(key);
  }

  public Optional<Button> button(String label) {
    return rootLayout.getWidgets(Toolbar.class)
        .flatMap(it -> it.buttons().stream())
        .filter(it -> label.equals(it.label()))
        .findFirst();
  }

  @Override
  public String toString() {
    final var sb = new StringBuilder("View [ name: " + name + " ][ id: " + id + " ]");
    if (dialogs.isEmpty()) {
      return sb.toString();
    }
  
    sb.append("\nDialogs:");
    for (var dialog : dialogs) {
      sb.append(StringUtil.toIndentedString("\n- " + dialog.toString()));
    }
    return sb.toString();

  }
}
