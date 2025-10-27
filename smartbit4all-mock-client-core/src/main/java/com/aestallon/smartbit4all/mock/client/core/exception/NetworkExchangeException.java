package com.aestallon.smartbit4all.mock.client.core.exception;

public final class NetworkExchangeException extends ClientException {

  public NetworkExchangeException(ClientExceptionContext ctx) {
    super(ctx);
  }
  
  public NetworkExchangeException(ClientExceptionContext ctx, Throwable cause) {
    super(ctx, cause);
  }

}
