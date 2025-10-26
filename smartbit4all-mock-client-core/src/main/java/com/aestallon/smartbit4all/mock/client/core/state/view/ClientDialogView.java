package com.aestallon.smartbit4all.mock.client.core.state.view;

import com.aestallon.smartbit4all.mock.client.core.client.MockClient;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewId;

public final class ClientDialogView extends ClientView {

  ClientDialogView(MockClient client, ViewId id, String name, ClientView parent) {
    super(client, id, name, parent);
  }

}
