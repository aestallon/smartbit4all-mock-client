package com.aestallon.smartbit4all.mock.client.core.api.impl;

import java.util.List;
import static org.assertj.core.api.Fail.fail;
import org.smartbit4all.api.view.bean.ComponentModel;
import org.smartbit4all.api.view.bean.DataChange;
import org.smartbit4all.api.view.bean.UiActionRequest;
import org.smartbit4all.api.view.bean.ViewConstraint;
import org.smartbit4all.api.view.bean.ViewContextChange;
import org.smartbit4all.api.view.bean.ViewContextData;
import org.smartbit4all.api.view.bean.ViewContextUpdate;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.aestallon.smartbit4all.mock.client.core.api.ComponentAPI;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.NodeId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.SmartLinkId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewContextId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.WidgetId;
import com.aestallon.smartbit4all.mock.client.core.client.InteractionContext;

public class MockComponentAPI extends AbstractAPI implements ComponentAPI {

  public MockComponentAPI(WebTestClient client, 
                          RequestContext context,
                          InteractionContext interactionContext) {
    super(client, context, interactionContext);
  }

  @Override
  public ViewContextData createViewContext() {
    return post(RequestSpec.of(UriSpec.of("/context"), ViewContextData.class));
  }

  @Override
  public ViewContextData updateViewContext(ViewContextUpdate viewContextUpdate) {
    return put(RequestSpec.of(UriSpec.of("/context"), ViewContextData.class, viewContextUpdate));
  }

  @Override
  public ViewContextData getViewContext(ViewContextId viewContextId) {
    return get(RequestSpec.of(UriSpec.of("/context/{uuid}", viewContextId), ViewContextData.class));
  }

  @Override
  public ViewContextData message(ViewId viewId, ViewId messageId, String messageResult) {
    return post(RequestSpec.of(
        UriSpec.of("/message/{viewUuid}/{messageUuid}", viewId, messageId),
        ViewContextData.class,
        messageResult
    ));
  }

  @Override
  public ViewConstraint getViewConstraint(ViewId viewId) {
    return get(RequestSpec.of(
        UriSpec.of("/view/{uuid}/constraint", viewId),
        ViewConstraint.class));
  }

  @Override
  public void showPublishedView(String channel, SmartLinkId smartLinkId) {
    put(RequestSpec.of(UriSpec.of("/smartlink/{channel}/{uuid}", channel, smartLinkId)));
  }

  @Override
  public Resource downloadItem(ViewId viewId, String item) {
    return fail("Not yet implemented");
  }

  @Override
  public ComponentModel getComponentModel(ViewId viewId) {
    return get(RequestSpec.of(
        UriSpec.of("/component/{uuid}", viewId.uuid()),
        ComponentModel.class));
  }

  @Override
  public ViewContextChange getComponentModel2(ViewId viewId) {
    return get(RequestSpec.of(
        UriSpec.of("/component/{uuid}/load", viewId.uuid()),
        ViewContextChange.class));
  }

  @Override
  public ViewContextChange performAction(ViewId viewId, UiActionRequest request) {
    return post(RequestSpec.of(
        UriSpec.of("/component/{uuid}/action", viewId),
        ViewContextChange.class,
        request));
  }

  @Override
  public ViewContextChange performWidgetMainAction(ViewId viewId, WidgetId widgetId,
                                                   UiActionRequest request) {
    return post(RequestSpec.of(
        UriSpec.of("/component/{uuid}/{widgetId}/action", viewId, widgetId),
        ViewContextChange.class,
        request));
  }

  @Override
  public ViewContextChange performWidgetAction(ViewId viewId,
                                               WidgetId widgetId,
                                               NodeId nodeId,
                                               UiActionRequest request) {
    return post(RequestSpec.of(
        UriSpec.of("/component/{uuid}/{widgetId}/{nodeId}/action", viewId, widgetId, nodeId),
        ViewContextChange.class,
        request));
  }

  @Override
  public ViewContextChange dataChanged(ViewId viewId, DataChange dataChangeEvent) {
    return post(RequestSpec.of(
        UriSpec.of("/component/{uuid}/data", viewId.uuid()),
        ViewContextChange.class,
        dataChangeEvent));
  }

  @Override
  public ViewContextChange uploadAction(ViewId viewId, String uiActionRequest, String param,
                                        MockMultipartFile content) {
    return fail("Not yet implemented");
  }

  @Override
  public ViewContextChange uploadMultipleAction(ViewId viewId, String uiActionRequest, String param,
                                                List<MockMultipartFile> contents) {
    return fail("Not yet implemented");
  }
}
