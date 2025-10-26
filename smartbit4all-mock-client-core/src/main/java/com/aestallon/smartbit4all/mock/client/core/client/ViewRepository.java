package com.aestallon.smartbit4all.mock.client.core.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.Fail;
import org.smartbit4all.api.view.bean.ViewData;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewId;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientNormalView;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

final class ViewRepository {

  private final MockClient client;
  private ClientNormalView root;
  private final Map<ViewId, ClientView> views = new HashMap<>();

  ViewRepository(MockClient client) {
    this.client = client;
  }

  private boolean hasRoot() {
    return root != null;
  }

  ClientView add(ViewData viewData) {
    final ViewId id = new ViewId(viewData.getUuid());
    final ClientView view;
    switch (viewData.getType()) {
      case NORMAL -> {
        if (hasRoot()) {
          ClientNormalView parent = root;
          while (parent.child() != null) {
            parent = parent.child();
          }
          view = parent.openChild(viewData);
        } else {
          views.clear();
          root = ClientNormalView.root(client, viewData);
          view = root;
        }
      }
      case DIALOG -> {
        final ViewId parentId = new ViewId(viewData.getContainerUuid());
        final ClientView parent = views.get(parentId);
        view = parent.openDialog(viewData);
      }
      default -> {
        Fail.fail("Cannot create view, for unknown view type: " + viewData.getType());
        throw new AssertionError("Unreachable code");
      }
    }
    views.put(id, view);
    return view;
  }

  void close(ViewId id) {
    final ClientView view = views.get(id);
    if (view == null) {
      //
      System.out.println("Cannot close view, for cannot find view by ID: " + id);
    } else {
      view.close();
      views.remove(id);
    }
  }

  ClientView get(ViewId id) {
    return views.get(id);
  }
  
  Optional<ClientView> find(String viewName) {
    return views.values().stream().filter(it -> it.name().equals(viewName)).findFirst();
  }

}
