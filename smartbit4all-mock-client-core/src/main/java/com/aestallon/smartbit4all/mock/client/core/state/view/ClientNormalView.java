package com.aestallon.smartbit4all.mock.client.core.state.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.smartbit4all.api.view.bean.ViewData;
import com.aestallon.smartbit4all.mock.client.core.MockClient;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewId;

public final class ClientNormalView extends ClientView {

  public static ClientNormalView root(MockClient client, ViewData viewData) {
    return new ClientNormalView(
        client,
        new ViewId(viewData.getUuid()),
        viewData.getViewName());
  }

  private ClientNormalView child;

  private ClientNormalView(MockClient client, ViewId id, String name) {
    super(client, id, name);
  }

  private ClientNormalView(MockClient client, ViewId id, String name, ClientNormalView parent) {
    super(client, id, name, parent);
  }

  public ClientNormalView openChild(ViewData viewData) {
    final var childId = new ViewId(viewData.getUuid());
    final var name = viewData.getViewName();
    final var parentId = new ViewId(viewData.getContainerUuid());
    assertThat(parentId).isEqualTo(id);

    child = new ClientNormalView(client, childId, name, this);
    return child;
  }



  public void drop(ClientView view) {
    super.drop(view);
    if (view == child) {
      child = null;
    }
  }

  public ClientNormalView child() {
    return child;
  }

  public List<ClientDialogView> dialogs() {
    return Collections.unmodifiableList(dialogs);
  }

}
