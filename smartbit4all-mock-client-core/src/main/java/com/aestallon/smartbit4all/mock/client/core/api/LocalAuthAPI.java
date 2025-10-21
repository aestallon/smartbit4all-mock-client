package com.aestallon.smartbit4all.mock.client.core.api;

import org.smartbit4all.api.localauthentication.bean.LocalAuthenticationLoginRequest;

public interface LocalAuthAPI {

  void login(LocalAuthenticationLoginRequest request);

  void logout();

}
