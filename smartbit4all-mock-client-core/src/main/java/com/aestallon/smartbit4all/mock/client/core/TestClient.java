package com.aestallon.smartbit4all.mock.client.core;

import java.util.UUID;
import com.aestallon.smartbit4all.mock.client.core.assertj.ViewHandle;

public interface TestClient {

  ViewHandle view(String name);

  ViewHandle view(UUID uuid);

}
