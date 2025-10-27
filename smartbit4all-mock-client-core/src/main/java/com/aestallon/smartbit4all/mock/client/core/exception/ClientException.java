package com.aestallon.smartbit4all.mock.client.core.exception;

public abstract sealed class ClientException
    extends RuntimeException
    permits InvalidOperationException, NetworkExchangeException {

  protected final ClientExceptionContext ctx;

  protected ClientException(ClientExceptionContext ctx) {
    super();
    this.ctx = ctx;
  }

  protected ClientException(ClientExceptionContext ctx, Throwable cause) {
    super(cause);
    this.ctx = ctx;
  }

  public ClientExceptionContext ctx() {
    return ctx;
  }

}
