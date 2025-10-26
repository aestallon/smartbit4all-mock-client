package com.aestallon.smartbit4all.mock.client.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.Fail;
import org.assertj.core.util.Strings;
import org.smartbit4all.api.localauthentication.bean.LocalAuthenticationLoginRequest;
import org.smartbit4all.api.session.bean.SessionInfoData;
import org.smartbit4all.api.view.bean.ComponentModel;
import org.smartbit4all.api.view.bean.ComponentModelChange;
import org.smartbit4all.api.view.bean.View;
import org.smartbit4all.api.view.bean.ViewContextChange;
import org.smartbit4all.api.view.bean.ViewContextData;
import org.smartbit4all.api.view.bean.ViewContextUpdate;
import org.smartbit4all.api.view.bean.ViewData;
import org.smartbit4all.api.view.bean.ViewState;
import org.smartbit4all.api.view.bean.ViewStateUpdate;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;
import com.aestallon.smartbit4all.mock.client.core.api.ComponentAPI;
import com.aestallon.smartbit4all.mock.client.core.api.GridAPI;
import com.aestallon.smartbit4all.mock.client.core.api.LocalAuthAPI;
import com.aestallon.smartbit4all.mock.client.core.api.SessionAPI;
import com.aestallon.smartbit4all.mock.client.core.api.impl.CustomAPI;
import com.aestallon.smartbit4all.mock.client.core.api.impl.MockComponentAPI;
import com.aestallon.smartbit4all.mock.client.core.api.impl.MockGridAPI;
import com.aestallon.smartbit4all.mock.client.core.api.impl.MockLocalAuthAPI;
import com.aestallon.smartbit4all.mock.client.core.api.impl.MockSessionAPI;
import com.aestallon.smartbit4all.mock.client.core.api.impl.RequestContext;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewContextId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewId;
import com.aestallon.smartbit4all.mock.client.core.state.component.layout.Toolbar;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientNormalView;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.errorprone.annotations.Var;

public class MockClient {

  public record APIFactory(MockClient client) {

    public ComponentAPI component() {
      return new MockComponentAPI(client.webClient, client.requestContext());
    }

    public SessionAPI session() {
      return new MockSessionAPI(client.webClient, client.requestContext());
    }

    public LocalAuthAPI localAuth() {
      return new MockLocalAuthAPI(client.webClient, client.requestContext());
    }

    public GridAPI grid() {
      return new MockGridAPI(client.webClient, client.requestContext());
    }

    public CustomAPI custom() {
      return new CustomAPI(client.webClient, client.requestContext());
    }

  }


  public static final class Builder {
    private static final String BASE_PATH_DEFAULT = "/api";

    private final WebTestClient httpClient;

    private String viewApiBasePath = BASE_PATH_DEFAULT;
    private String gridApiBasePath = BASE_PATH_DEFAULT;
    private String localAuthBasePath = BASE_PATH_DEFAULT;
    private String sessionBasePath = BASE_PATH_DEFAULT;

    private Consumer<MockClient> authenticator;
    private String launchEndpoint;

    private String token;
    private ViewContextId viewContextId;

    private boolean defaultInitialization;

    private Builder(WebApplicationContext applicationContext) {
      this(WebTestClient.bindToApplicationContext(applicationContext).configureClient().build());
    }

    private Builder(WebTestClient httpClient) {
      this.httpClient = Objects.requireNonNull(httpClient);
    }

    public Builder withLocalAuthentication(String username, String password) {
      authenticator = client -> client.api
          .localAuth()
          .login(new LocalAuthenticationLoginRequest()
              .username(username)
              .password(password));
      return this;
    }

    public Builder withDefaultInitialization() {
      this.defaultInitialization = true;
      return this;
    }

    public Builder withLaunchCall(String endpoint) {
      this.launchEndpoint = endpoint;
      return this;
    }

    public Builder withToken(String token) {
      this.token = token;
      return this;
    }

    public Builder withViewContext(ViewContextId viewContextId) {
      this.viewContextId = viewContextId;
      return this;
    }

    public Builder viewApiBasePath(String basePath) {
      this.viewApiBasePath = basePath;
      return this;
    }

    public Builder gridApiBasePath(String basePath) {
      this.gridApiBasePath = basePath;
      return this;
    }

    public Builder localAuthBasePath(String basePath) {
      this.localAuthBasePath = basePath;
      return this;
    }

    public Builder sessionBasePath(String basePath) {
      this.sessionBasePath = basePath;
      return this;
    }

    public MockClient build() {

      final var actor = new MockClient(httpClient);
      if (defaultInitialization) {
        actor.startSession();

        if (authenticator != null) {
          authenticator.accept(actor);
          actor.getSession();
        }

        actor.startViewContext();
      }

      if (!Strings.isNullOrEmpty(launchEndpoint)) {
        actor.api.custom().get(launchEndpoint);
      }

      actor.syncViewContext();
      return actor;
    }
  }


  private static final class ViewRepository {

    private final MockClient client;
    private ClientNormalView root;
    private final Map<ViewId, ClientView> views = new HashMap<>();

    private ViewRepository(MockClient client) {
      this.client = client;
    }

    private boolean hasRoot() {
      return root != null;
    }

    private ClientView add(ViewData viewData) {
      final ViewId id = new ViewId(viewData.getUuid());
      final ClientView view;
      switch (viewData.getType()) {
        case NORMAL -> {
          if (hasRoot()) {
            ClientNormalView parent = root;
            while (parent.child() != null) {
              parent = parent.child();
            }
            view = parent.openChild(viewData);
          } else {
            views.clear();
            root = ClientNormalView.root(client, viewData);
            view = root;
          }
        }
        case DIALOG -> {
          final ViewId parentId = new ViewId(viewData.getContainerUuid());
          final ClientView parent = views.get(parentId);
          view = parent.openDialog(viewData);
        }
        default -> {
          Fail.fail("Cannot create view, for unknown view type: " + viewData.getType());
          throw new AssertionError("Unreachable code");
        }
      }
      views.put(id, view);
      return view;
    }

    private void close(ViewId id) {
      final ClientView view = views.get(id);
      if (view == null) {
        //
        System.out.println("Cannot close view, for cannot find view by ID: " + id);
      } else {
        view.close();
        views.remove(id);
      }
    }

    private ClientView get(ViewId id) {
      return views.get(id);
    }

  }


  public static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .build()
      .registerModule(new JavaTimeModule());

  public static Builder local(WebApplicationContext applicationContext) {
    return new Builder(applicationContext);
  }

  public static Builder remote(WebTestClient client) {
    return new Builder(client);
  }

  private final WebTestClient webClient;
  private final APIFactory api;
  private final ViewRepository repository;

  private SessionInfoData sessionInfoData;
  private ViewContextId viewContextId;

  private MockClient(WebTestClient webClient) {
    this.webClient = webClient;
    this.api = new APIFactory(this);
    this.repository = new ViewRepository(this);
  }

  private RequestContext requestContext() {
    return new RequestContext(
        sessionInfoData == null ? null : sessionInfoData.getSid(),
        viewContextId,
        "/api");
  }

  private void startSession() {
    sessionInfoData = api.session().startSession();
  }

  private void getSession() {
    sessionInfoData = api.session().getSession();
  }

  private void startViewContext() {
    ViewContextData viewContext = api.component().createViewContext();
    assertThat(viewContext)
        .withFailMessage(() -> "Failure to create view context!")
        .isNotNull();
    viewContextId = new ViewContextId(viewContext.getUuid());
  }

  private void syncViewContext() {
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
    return new ViewHandle(name);
  }


  // --- Minimal wrapper types for test-time fluent calls ---
  public static final class ViewHandle {
    private final String name;

    private ViewHandle(String name) { this.name = name; }

    public Optional<ViewHandle> asOptional() { return Optional.of(this); }

    public ButtonHandle button(String label) { return new ButtonHandle(label); }

    public FieldHandle field(String id) { return new FieldHandle(id); }

    public GridHandle grid(String id) { return new GridHandle(id); }

    @Override
    public String toString() { return "ViewHandle{" + name + '}'; }
  }


  public static final class ButtonHandle {
    private final String label;

    private ButtonHandle(String label) { this.label = label; }

    public void click() { /* no-op scaffold */ }

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
