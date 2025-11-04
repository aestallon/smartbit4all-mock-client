package com.aestallon.smartbit4all.mock.client.core.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.smartbit4all.api.view.bean.ViewData;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewId;
import com.aestallon.smartbit4all.mock.client.core.exception.InvalidOperationException;
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

  ClientView open(InteractionContext ctx, ViewData viewData) {
    final ViewId id = new ViewId(viewData.getUuid());
    ctx.push("Opening View [ name: " + viewData.getViewName() + " ][ id: " + id + " ]");
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
      default -> throw new InvalidOperationException(
          ctx, 
          "Unsupported view type: " + viewData.getType());
    }
    views.put(id, view);
    return view;
  }

  void close(InteractionContext ctx, ViewId id) {
    final ClientView view = views.get(id);
    if (view == null) {
      ctx.push("Tried to close View [ " + id + " ], but no such view exists!");
    } else {
      view.close();
      views.remove(id);
      ctx.push("Closed " + view);
    }
  }

  ClientView get(ViewId id) {
    return views.get(id);
  }
  
  Optional<ClientView> find(String viewName) {
    return views.values().stream().filter(it -> it.name().equals(viewName)).findFirst();
  }
  
  String report() {
    return hasRoot() ? root.toString() : "No views present";
  }

}
