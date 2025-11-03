package com.aestallon.smartbit4all.mock.client.core.api.impl;

import java.util.UUID;
import org.smartbit4all.api.grid.bean.GridModel;
import org.smartbit4all.api.grid.bean.GridSelectionChange;
import org.smartbit4all.api.grid.bean.GridUpdateData;
import org.smartbit4all.api.view.bean.ViewContextChange;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.aestallon.smartbit4all.mock.client.core.api.GridAPI;
import com.aestallon.smartbit4all.mock.client.core.client.InteractionContext;

/**
 * WebTestClient-backed implementation of GridAPI.
 *
 * Note: Endpoint paths are inferred from conventions used by MockComponentAPI.
 * They are prefixed with the basePath provided via RequestContext.
 */
public class MockGridAPI extends AbstractAPI implements GridAPI {

  public MockGridAPI(WebTestClient client, RequestContext context,
                     InteractionContext interactionContext) {
    super(client, context, interactionContext);
  }

  @Override
  public GridModel load(UUID uuid, String gridIdentifier) {
    return get(RequestSpec.of(
        UriSpec.of("/component/{uuid}/grid/{gridId}", uuid, gridIdentifier),
        GridModel.class));
  }

  @Override
  public void setPage(UUID uuid, String gridId, String offsetStr, String limitStr) {
    put(RequestSpec.of(
        UriSpec.of("/component/{uuid}/grid/{gridId}/page/{offset}/{limit}", uuid, gridId, offsetStr, limitStr)));
  }

  @Override
  public void update(UUID uuid, String gridId, GridUpdateData gridUpdateData) {
    put(RequestSpec.of(
        UriSpec.of("/component/{uuid}/grid/{gridId}", uuid, gridId),
        Void.class,
        gridUpdateData));
  }

  @Override
  public Object expand(UUID uuid, String gridId, String rowId) {
    return get(RequestSpec.of(
        UriSpec.of("/component/{uuid}/grid/{gridId}/row/{rowId}/expand", uuid, gridId, rowId),
        Object.class));
  }

  @Override
  public ViewContextChange select(UUID uuid, String gridId, String rowId, boolean selected) {
    return put(RequestSpec.of(
        UriSpec.of("/component/{uuid}/grid/{gridId}/row/{rowId}/select/{selected}", uuid, gridId, rowId, selected),
        ViewContextChange.class));
  }

  @Override
  public ViewContextChange selectRows(UUID uuid, String gridId, GridSelectionChange gridSelectionChange) {
    return put(RequestSpec.of(
        UriSpec.of("/component/{uuid}/grid/{gridId}/select", uuid, gridId),
        ViewContextChange.class,
        gridSelectionChange));
  }

  @Override
  public ViewContextChange selectAll(UUID uuid, String gridId, boolean selected) {
    return put(RequestSpec.of(
        UriSpec.of("/component/{uuid}/grid/{gridId}/selectAll/{selected}", uuid, gridId, selected),
        ViewContextChange.class));
  }
}
