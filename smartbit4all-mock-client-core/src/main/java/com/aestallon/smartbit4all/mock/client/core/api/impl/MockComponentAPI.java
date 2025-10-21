package com.aestallon.smartbit4all.mock.client.core.api.impl;

import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.smartbit4all.api.view.bean.ComponentModel;
import org.smartbit4all.api.view.bean.DataChange;
import org.smartbit4all.api.view.bean.UiActionRequest;
import org.smartbit4all.api.view.bean.ViewConstraint;
import org.smartbit4all.api.view.bean.ViewContextChange;
import org.smartbit4all.api.view.bean.ViewContextData;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import com.aestallon.smartbit4all.mock.client.core.api.ComponentAPI;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MockComponentAPI implements ComponentAPI {

  private final MockMvcTester mvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public MockComponentAPI(MockMvcTester mvc) {
    this.mvc = mvc;
  }

  @Override
  public ViewContextData createViewContext() {
    MvcTestResult result = mvc.post()
        .uri("/context")
        .contentType("application/json")
        .exchange();
    assertThat(result)
        .hasStatusOk()
        .hasContentTypeCompatibleWith(MediaType.APPLICATION_JSON);
    assertThat(result.getUnresolvedException()).isNull();
    final var response = result.getResponse();
    final var body = new String(
        response.getContentAsByteArray(),
        Charset.forName(response.getCharacterEncoding()));
    objectMapper.readValue(body, new TypeReference<ViewContextData>() {});
    return null;
  }

  @Override
  public ViewContextData updateViewContext(ViewContextData viewContextData) {
    return null;
  }

  @Override
  public ViewContextData getViewContext(UUID uuid) {
    return null;
  }

  @Override
  public ViewContextData message(String viewUuid, String messageUuid, String messageResult) {
    return null;
  }

  @Override
  public ViewConstraint getViewConstraint(UUID uuid) {
    return null;
  }

  @Override
  public void showPublishedView(String channel, UUID uuid) {

  }

  @Override
  public Resource downloadItem(UUID uuid, String item) {
    return null;
  }

  @Override
  public ComponentModel getComponentModel(UUID uuid) {
    return null;
  }

  @Override
  public ViewContextChange getComponentModel2(UUID uuid) {
    return null;
  }

  @Override
  public ViewContextChange performAction(UUID uuid, UiActionRequest request) {
    return null;
  }

  @Override
  public ViewContextChange performWidgetMainAction(UUID uuid, String widgetId,
                                                   UiActionRequest request) {
    return null;
  }

  @Override
  public ViewContextChange performWidgetAction(UUID uuid, String widgetId, String nodeId,
                                               UiActionRequest request) {
    return null;
  }

  @Override
  public ViewContextChange dataChanged(UUID uuid, DataChange dataChangeEvent) {
    return null;
  }

  @Override
  public ViewContextChange uploadAction(UUID uuid, String uiActionRequest, String param,
                                        MockMultipartFile content) {
    return null;
  }

  @Override
  public ViewContextChange uploadMultipleAction(UUID uuid, String uiActionRequest, String param,
                                                List<MockMultipartFile> contents) {
    return null;
  }
}
