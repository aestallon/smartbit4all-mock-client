package com.aestallon.smartbit4all.mock.client.core.exception;

import com.aestallon.smartbit4all.mock.client.core.client.InteractionContext;

public final class InvalidOperationException extends ClientException {

  public InvalidOperationException(InteractionContext ctx, String message) {
    super(ctx, message);
  }
  
  public InvalidOperationException(InteractionContext ctx, Throwable cause) {
    super(ctx, cause);
  }
  
}
