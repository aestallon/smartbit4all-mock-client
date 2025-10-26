package com.aestallon.smartbit4all.mock.client.core.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.Fail;
import org.smartbit4all.api.session.bean.SessionInfoData;
import org.smartbit4all.api.view.bean.ComponentModel;
import org.smartbit4all.api.view.bean.ComponentModelChange;
import org.smartbit4all.api.view.bean.UiActionRequest;
import org.smartbit4all.api.view.bean.ViewContextChange;
import org.smartbit4all.api.view.bean.ViewContextData;
import org.smartbit4all.api.view.bean.ViewContextUpdate;
import org.smartbit4all.api.view.bean.ViewData;
import org.smartbit4all.api.view.bean.ViewState;
import org.smartbit4all.api.view.bean.ViewStateUpdate;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;
import com.aestallon.smartbit4all.mock.client.core.api.impl.RequestContext;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewContextId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewId;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MockClient {


  public static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .build()
      .registerModule(new JavaTimeModule());

  public static MockClientBuilder local(WebApplicationContext applicationContext) {
    return new MockClientBuilder(applicationContext);
  }

  public static MockClientBuilder remote(WebTestClient client) {
    return new MockClientBuilder(client);
  }

  private final WebTestClient webClient;
  private final APIFactory api;
  private final ViewRepository repository;

  private SessionInfoData sessionInfoData;
  private ViewContextId viewContextId;

  MockClient(WebTestClient webClient) {
    this.webClient = webClient;
    this.api = new APIFactory(this);
    this.repository = new ViewRepository(this);
  }

  RequestContext requestContext() {
    return new RequestContext(
        sessionInfoData == null ? null : sessionInfoData.getSid(),
        viewContextId,
        "/api");
  }

  WebTestClient webClient() {
    return webClient;
  }

  void startSession() {
    sessionInfoData = api.session().startSession();
  }

  void getSession() {
    sessionInfoData = api.session().getSession();
  }

  void startViewContext() {
    ViewContextData viewContext = api.component().createViewContext();
    assertThat(viewContext)
        .withFailMessage(() -> "Failure to create view context!")
        .isNotNull();
    viewContextId = new ViewContextId(viewContext.getUuid());
  }

  void syncViewContext() {
    final var viewContext = api.component().getViewContext(viewContextId);
    assertThat(viewContext.getUuid()).isEqualTo(viewContextId.uuid());
    onViewContextChange(viewContext);
  }

  public APIFactory api() {
    return api;
  }

  public void onViewContextChange(ViewContextChange change) {
    onComponentModelChanges(change.getChanges());
    onViewContextChange(change.getViewContext());

  }

  private void onComponentModelChanges(List<ComponentModelChange> changes) {
    if (changes == null || changes.isEmpty()) {
      return;
    }

    for (final var change : changes) {
      final ClientView clientView = repository.get(new ViewId(change.getUuid()));
      if (clientView == null) {
        Fail.fail("Cannot process Component model change, for cannot find view by ID: "
                  + change.getUuid());
        return;
      }
      Object o = change.getChanges().get("");
      if (o instanceof ComponentModel model) {
        clientView.componentModel(model);
      } else if (o instanceof Map<?, ?> m) {
        try {
          final var model = OBJECT_MAPPER.readValue(
              OBJECT_MAPPER.writeValueAsBytes(m),
              ComponentModel.class);
          clientView.componentModel(model);
        } catch (IOException e) {
          Fail.fail(e);
        }
      }
    }
  }

  private void onViewContextChange(ViewContextData viewContext) {
    List<ViewData> toClose = new ArrayList<>();
    List<ViewData> toOpen = new ArrayList<>();
    List<ViewId> opened = new ArrayList<>();
    for (final var viewData : viewContext.getViews()) {
      switch (viewData.getState()) {
        case TO_CLOSE -> toClose.add(viewData);
        case TO_OPEN -> toOpen.add(viewData);
        case OPENED -> opened.add(new ViewId(viewData.getUuid()));
      }
    }

    opened.stream().map(repository::get).forEach(ClientView::ensureLoaded);
    List<ViewId> closedIds = toClose.stream().map(ViewData::getUuid).map(ViewId::new).toList();
    List<ClientView> openedViews = toOpen.stream().map(repository::add).toList();

    final var update = new ViewContextUpdate()
        .uuid(viewContextId.uuid())
        .updates(Stream
            .concat(
                closedIds.stream()
                    .map(it -> new ViewStateUpdate()
                        .uuid(it.uuid())
                        .state(ViewState.CLOSED)),
                openedViews.stream()
                    .map(it -> new ViewStateUpdate()
                        .uuid(it.id().uuid())
                        .state(ViewState.OPENED)))
            .toList());
    if (update.getUpdates().isEmpty()) {
      return;
    }

    final var result = api.component().updateViewContext(update);
    onViewContextChange(result);
  }

  // Minimal view scaffolding to allow fluent test code to compile
  public ViewHandle view(String name) {
    return new ViewHandle(name, this);
  }


  // --- Minimal wrapper types for test-time fluent calls ---
  public static final class ViewHandle {
    private final String name;
    private final MockClient client;

    private ViewHandle(String name, MockClient client) {
      this.name = name;
      this.client = client;
    }

    public Optional<ViewHandle> asOptional() { return Optional.of(this); }

    public ButtonHandle button(String label) {
      return new ButtonHandle(label, this, client);
    }

    public FieldHandle field(String id) { return new FieldHandle(id); }

    public GridHandle grid(String id) { return new GridHandle(id); }

    @Override
    public String toString() { return "ViewHandle{" + name + '}'; }
  }


  public static final class ButtonHandle {
    private final String label;
    private final ViewHandle parent;
    private final MockClient client;

    private ButtonHandle(String label, ViewHandle parent, MockClient client) {
      this.label = label;
      this.parent = parent;
      this.client = client;
    }

    public void click() {
      client.repository.find(parent.name)
          .ifPresent(view -> view.action(label)
              .ifPresent(action -> {
                final var change = client
                    .api().component()
                    .performAction(view.id(), new UiActionRequest()
                        .code(action.getCode())
                        .putParamsItem("model", view.componentModel().getData()));
                client.onViewContextChange(change);
              }));
    }

    public boolean isEnabled() { return true; }

    @Override
    public String toString() { return "Button{" + label + '}'; }
  }


  public static final class FieldHandle {
    private final String id;
    private String value;

    private FieldHandle(String id) { this.id = id; }

    public void setValue(String value) { this.value = value; }

    public String getValue() { return value; }

    @Override
    public String toString() { return "Field{" + id + '=' + value + '}'; }
  }


  public static final class GridHandle {
    private final String id;

    private GridHandle(String id) { this.id = id; }

    @Override
    public String toString() { return "Grid{" + id + '}'; }
  }
}
