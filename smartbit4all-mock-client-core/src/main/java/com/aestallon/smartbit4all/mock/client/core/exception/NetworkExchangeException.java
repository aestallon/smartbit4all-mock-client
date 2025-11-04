package com.aestallon.smartbit4all.mock.client.core.exception;

import com.aestallon.smartbit4all.mock.client.core.client.InteractionContext;

public final class NetworkExchangeException extends ClientException {

  public NetworkExchangeException(InteractionContext ctx, String message) {
    super(ctx, message);
  }
  
  public NetworkExchangeException(InteractionContext ctx, Throwable cause) {
    super(ctx, cause);
  }

}
