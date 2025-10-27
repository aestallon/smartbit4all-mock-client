package com.aestallon.smartbit4all.mock.client.core.exception;

public final class InvalidOperationException extends ClientException {

  public InvalidOperationException(ClientExceptionContext ctx) {
    super(ctx);
  }
  
  public InvalidOperationException(ClientExceptionContext ctx, Throwable cause) {
    super(ctx, cause);
  }
  
}
