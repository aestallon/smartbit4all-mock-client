package com.aestallon.smartbit4all.mock.client.core.api.newtype;

public record WidgetId(String strVal) {

  @Override
  public String toString() {
    return strVal;
  }
  
}
