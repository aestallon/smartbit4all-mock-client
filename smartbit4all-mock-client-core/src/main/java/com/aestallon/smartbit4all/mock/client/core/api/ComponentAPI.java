package com.aestallon.smartbit4all.mock.client.core.api;

import java.util.List;
import java.util.UUID;
import org.smartbit4all.api.view.bean.ComponentModel;
import org.smartbit4all.api.view.bean.DataChange;
import org.smartbit4all.api.view.bean.UiActionRequest;
import org.smartbit4all.api.view.bean.ViewConstraint;
import org.smartbit4all.api.view.bean.ViewContextChange;
import org.smartbit4all.api.view.bean.ViewContextData;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

public interface ComponentAPI {

  ViewContextData createViewContext();

  ViewContextData updateViewContext(ViewContextData viewContextData);

  ViewContextData getViewContext(UUID uuid);

  ViewContextData message(String viewUuid, String messageUuid, String messageResult);

  ViewConstraint getViewConstraint(UUID uuid);

  void showPublishedView(String channel, UUID uuid);

  Resource downloadItem(UUID uuid, String item);

  ComponentModel getComponentModel(UUID uuid);

  ViewContextChange getComponentModel2(UUID uuid);

  ViewContextChange performAction(UUID uuid, UiActionRequest request);

  ViewContextChange performWidgetMainAction(UUID uuid, String widgetId, UiActionRequest request);

  ViewContextChange performWidgetAction(UUID uuid,
                                        String widgetId,
                                        String nodeId,
                                        UiActionRequest request);

  ViewContextChange dataChanged(UUID uuid, DataChange dataChangeEvent);

  ViewContextChange uploadAction(UUID uuid, String uiActionRequest,
                                 String param, MockMultipartFile content);

  ViewContextChange uploadMultipleAction(UUID uuid, String uiActionRequest,
                                         String param, List<MockMultipartFile> contents);

}
