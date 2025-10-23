package com.aestallon.smartbit4all.mock.client.core.api.newtype;

import java.util.UUID;

public record SmartLinkId(UUID uuid) {

  @Override
  public String toString() {
    return uuid.toString();
  }
  
}
