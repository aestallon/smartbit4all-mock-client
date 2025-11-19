package com.aestallon.smartbit4all.mock.client.core.api.newtype;

/**
 * 
 * @param strVal
 */
public record NodeId(String strVal) {

  @Override
  public String toString() {
    return strVal;
  }

}
