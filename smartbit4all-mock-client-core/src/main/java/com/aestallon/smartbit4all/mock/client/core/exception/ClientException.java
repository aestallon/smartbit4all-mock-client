package com.aestallon.smartbit4all.mock.client.core.exception;

import org.assertj.core.api.Assertions;
import com.aestallon.smartbit4all.mock.client.core.client.InteractionContext;

public abstract sealed class ClientException
    extends RuntimeException
    permits InvalidOperationException, NetworkExchangeException {

  protected final InteractionContext ctx;

  protected ClientException(InteractionContext ctx, String message) {
    super(message);
    this.ctx = ctx;
  }

  protected ClientException(InteractionContext ctx, Throwable cause) {
    super(cause);
    this.ctx = ctx;
  }

  public InteractionContext ctx() {
    return ctx;
  }

  public void fail() {
    final String msg = "INTERACTION FAILED:\n"
                       + ctx() + "\n^^^^^^^^" + "\n    Error encountered here: "
                       + getMessage();
    final Throwable cause = getCause();
    if (cause != null) {
      Assertions.fail(msg, cause);
    } else {
      Assertions.fail(msg);
    }
  }
}
