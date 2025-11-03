package com.aestallon.smartbit4all.mock.client.core.assertj;

import com.aestallon.smartbit4all.mock.client.core.state.component.layout.Toolbar;

public class ToolbarAssert extends AbstractComponentAssert<ToolbarAssert, Toolbar> {

  protected ToolbarAssert(ComponentLocator<Toolbar> locator) {
    super(locator);
  }

  @Override
  protected ToolbarAssert self() {
    return this;
  }

  public ToolbarAssert containsButtonLabelled(String label) {
    return checkOnPresentOrFail(
        toolbar -> {
          // TODO: Implement
        },
        "");
  }

}
