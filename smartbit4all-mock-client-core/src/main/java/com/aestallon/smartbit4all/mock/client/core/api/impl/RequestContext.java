package com.aestallon.smartbit4all.mock.client.core.api.impl;

import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewContextId;
import com.aestallon.smartbit4all.mock.client.core.client.InteractionContext;

public record RequestContext(
    String token, 
    ViewContextId viewContextId, 
    String basePath,
    InteractionContext interactionContext) {}
