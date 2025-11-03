package com.aestallon.smartbit4all.mock.client.core.assertj;

import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public class ViewAssert extends AbstractComponentAssert<ViewAssert, ClientView> {
  
  protected ViewAssert(ComponentLocator<ClientView> locator) {
    super(locator);
  }

  @Override
  protected ViewAssert self() {
    return this;
  }
}
