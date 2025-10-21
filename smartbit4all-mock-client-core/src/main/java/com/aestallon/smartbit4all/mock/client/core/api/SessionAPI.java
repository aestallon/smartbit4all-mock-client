package com.aestallon.smartbit4all.mock.client.core.api;

import org.smartbit4all.api.session.bean.GetAuthenticationProvidersResponse;
import org.smartbit4all.api.session.bean.RefreshSessionRequest;
import org.smartbit4all.api.session.bean.SessionInfoData;

public interface SessionAPI {
  
  SessionInfoData startSession();
  
  SessionInfoData getSession();
  
  GetAuthenticationProvidersResponse getAuthenticationProviders();
  
  SessionInfoData refreshSession(RefreshSessionRequest refreshSessionRequest);
  
  SessionInfoData setLocale(String locale);
}
