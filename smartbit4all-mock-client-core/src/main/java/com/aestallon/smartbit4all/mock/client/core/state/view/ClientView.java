package com.aestallon.smartbit4all.mock.client.core.state.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.smartbit4all.api.view.bean.ComponentModel;
import org.smartbit4all.api.view.bean.ViewContextChange;
import org.smartbit4all.api.view.bean.ViewData;
import com.aestallon.smartbit4all.mock.client.core.MockClient;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.WidgetId;
import com.aestallon.smartbit4all.mock.client.core.state.ClientComponent;

public abstract sealed class ClientView
    extends ClientComponent
    permits ClientDialogView, ClientNormalView {

  protected ViewId id;
  protected String name;
  protected ClientView parent;
  protected final List<ClientDialogView> dialogs = new ArrayList<>();

  protected ComponentModel model;

  protected ClientView(MockClient client, ViewId id, String name) {
    this(client, id, name, null);
  }

  protected ClientView(MockClient client, ViewId id, String name, ClientView parent) {
    super(client);
    this.id = id;
    this.name = name;
    this.parent = parent;
  }

  public ViewId id() {
    return id;
  }

  public void id(ViewId id) {
    this.id = id;
  }

  public Optional<ClientView> parent() {
    return Optional.ofNullable(parent);
  }

  public void componentModel(ComponentModel componentModel) {
    this.model = componentModel;
  }

  public ComponentModel componentModel() {
    return model;
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

}
