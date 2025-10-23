package com.aestallon.smartbit4all.mock.client.core.api;

import java.util.List;
import java.util.UUID;
import org.smartbit4all.api.view.bean.ComponentModel;
import org.smartbit4all.api.view.bean.DataChange;
import org.smartbit4all.api.view.bean.UiActionRequest;
import org.smartbit4all.api.view.bean.ViewConstraint;
import org.smartbit4all.api.view.bean.ViewContextChange;
import org.smartbit4all.api.view.bean.ViewContextData;
import org.smartbit4all.api.view.bean.ViewContextUpdate;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.NodeId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.SmartLinkId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewContextId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.WidgetId;

public interface ComponentAPI {

  ViewContextData createViewContext();

  ViewContextData updateViewContext(ViewContextUpdate viewContextUpdate);

  ViewContextData getViewContext(ViewContextId viewContextId);

  ViewContextData message(ViewId viewId, ViewId messageId, String messageResult);

  ViewConstraint getViewConstraint(ViewId viewId);

  void showPublishedView(String channel, SmartLinkId smartLinkId);

  Resource downloadItem(ViewId viewId, String item);

  ComponentModel getComponentModel(ViewId viewId);

  ViewContextChange getComponentModel2(ViewId viewId);

  ViewContextChange performAction(ViewId viewId, UiActionRequest request);

  ViewContextChange performWidgetMainAction(ViewId viewId, WidgetId widgetId,
                                            UiActionRequest request);

  ViewContextChange performWidgetAction(ViewId viewId,
                                        WidgetId widgetId,
                                        NodeId nodeId,
                                        UiActionRequest request);

  ViewContextChange dataChanged(ViewId viewId, DataChange dataChangeEvent);

  ViewContextChange uploadAction(ViewId viewId, String uiActionRequest,
                                 String param, MockMultipartFile content);

  ViewContextChange uploadMultipleAction(ViewId viewId, String uiActionRequest,
                                         String param, List<MockMultipartFile> contents);

}
