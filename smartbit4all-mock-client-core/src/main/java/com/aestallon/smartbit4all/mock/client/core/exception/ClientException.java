package com.aestallon.smartbit4all.mock.client.core.exception;

import com.aestallon.smartbit4all.mock.client.core.client.InteractionContext;

public abstract sealed class ClientException
    extends RuntimeException
    permits InvalidOperationException, NetworkExchangeException {

  protected final InteractionContext ctx;

  protected ClientException(InteractionContext ctx) {
    super();
    this.ctx = ctx;
  }

  protected ClientException(InteractionContext ctx, Throwable cause) {
    super(cause);
    this.ctx = ctx;
  }

  public InteractionContext ctx() {
    return ctx;
  }

}
