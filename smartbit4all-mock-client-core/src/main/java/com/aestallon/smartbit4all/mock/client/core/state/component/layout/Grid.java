package com.aestallon.smartbit4all.mock.client.core.state.component.layout;

import com.aestallon.smartbit4all.mock.client.core.api.newtype.WidgetId;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;

public final class Grid extends DeferredInitWidget<Grid, Grid.Key> {

  public record Key(String strVal) implements WidgetKey<Grid> {

    @Override
    public WidgetId asId() {
      return new WidgetId(strVal);
    }

  }
  
  private final Toolbar toolbar;

  public Grid(ClientView view, Key key) {
    super(view, key);
    
    // every grid has a "default" toolbar:
    toolbar = new Toolbar(view, new Toolbar.Key.Custom(key.strVal() + "_toolbar"));
  }

  @Override
  public void init() {
    
  }
}
