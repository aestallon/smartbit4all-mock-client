package com.aestallon.smartbit4all.mock.client.core.api.newtype;

public record NodeId(String strVal) {

  @Override
  public String toString() {
    return strVal;
  }

}
