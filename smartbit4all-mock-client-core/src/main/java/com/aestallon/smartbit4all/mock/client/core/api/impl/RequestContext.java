package com.aestallon.smartbit4all.mock.client.core.api.impl;

import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewContextId;

public record RequestContext(String token, ViewContextId viewContextId, String basePath) {}
