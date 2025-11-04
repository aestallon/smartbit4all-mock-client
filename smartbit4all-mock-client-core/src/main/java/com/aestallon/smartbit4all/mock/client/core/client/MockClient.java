package com.aestallon.smartbit4all.mock.client.core.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
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
import com.aestallon.smartbit4all.mock.client.core.TestClient;
import com.aestallon.smartbit4all.mock.client.core.api.impl.RequestContext;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.NodeId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewContextId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.ViewId;
import com.aestallon.smartbit4all.mock.client.core.api.newtype.WidgetId;
import com.aestallon.smartbit4all.mock.client.core.assertj.ComponentLocationResult;
import com.aestallon.smartbit4all.mock.client.core.assertj.ViewHandle;
import com.aestallon.smartbit4all.mock.client.core.exception.NetworkExchangeException;
import com.aestallon.smartbit4all.mock.client.core.state.view.ClientView;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MockClient implements TestClient {


  public static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .build()
      .registerModule(new JavaTimeModule());

  public static <T> T coerceType(Object o, Class<T> type) {
    return OBJECT_MAPPER.convertValue(o, type);
  }

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

  RequestContext requestContext(InteractionContext interactionContext) {
    return new RequestContext(
        sessionInfoData == null ? null : sessionInfoData.getSid(),
        viewContextId,
        "/api",
        interactionContext);
  }

  WebTestClient webClient() {
    return webClient;
  }

  void startSession(InteractionContext interactionContext) {
    sessionInfoData = api
        .session(interactionContext.push("Starting Session"))
        .startSession();
  }

  void getSession(InteractionContext interactionContext) {
    sessionInfoData = api
        .session(interactionContext.push("Retrieving Session"))
        .getSession();
  }

  void startViewContext(InteractionContext interactionContext) {
    ViewContextData viewContext = api
        .component(interactionContext.push("Creating ViewContext"))
        .createViewContext();
    if (viewContext == null) {
      throw new NetworkExchangeException(interactionContext, "ViewContext was null!");
    }

    viewContextId = new ViewContextId(viewContext.getUuid());
  }

  void syncViewContext(InteractionContext interactionContext) {
    final var viewContext = api
        .component(interactionContext.push("Synchronising ViewContext"))
        .getViewContext(viewContextId);
    if (!Objects.equals(viewContextId.uuid(), viewContext.getUuid())) {
      throw new NetworkExchangeException(interactionContext, "ViewContext UUID mismatch!");
    }

    onViewContextChange(interactionContext, viewContext);
  }

  APIFactory api() {
    return api;
  }

  void onViewContextChange(InteractionContext interactionContext, ViewContextChange change) {
    onComponentModelChanges(interactionContext, change.getChanges());
    onViewContextChange(interactionContext, change.getViewContext());
  }

  private void onComponentModelChanges(InteractionContext interactionContext,
                                       List<ComponentModelChange> changes) {
    if (changes == null || changes.isEmpty()) {
      return;
    }

    for (final var change : changes) {
      final ViewId id = new ViewId(change.getUuid());
      interactionContext.push("Processing ComponentModelChange for " + id);

      final ClientView clientView = repository.get(id);
      if (clientView == null) {
        throw new NetworkExchangeException(interactionContext, "No view found for " + id);
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
          throw new NetworkExchangeException(interactionContext, e);
        }
      }
    }
  }

  private void onViewContextChange(InteractionContext interactionContext,
                                   ViewContextData viewContext) {
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

    final List<ViewStateUpdate> updates = new ArrayList<>();
    for (final var id : toClose.stream().map(ViewData::getUuid).map(ViewId::new).toList()) {
      repository.close(interactionContext, id);
      updates.add(new ViewStateUpdate()
          .uuid(id.uuid())
          .state(ViewState.CLOSED));
    }

    for (final var viewData : toOpen) {
      ClientView view = repository.open(interactionContext, viewData);
      updates.add(new ViewStateUpdate()
          .uuid(view.id().uuid())
          .state(ViewState.OPENED));
    }

    if (updates.isEmpty()) {
      ensureOpenViewsAreLoaded(interactionContext, opened);
      return;
    }

    final var result = api
        .component(interactionContext.push("Updating ViewContext"))
        .updateViewContext(new ViewContextUpdate()
            .uuid(viewContextId.uuid())
            .updates(updates));
    onViewContextChange(interactionContext, result);
  }

  private void ensureOpenViewsAreLoaded(InteractionContext interactionContext,
                                        Collection<ViewId> ids) {
    for (final var id : ids) {
      final ClientView view = repository.get(id);
      if (view == null || view.isLoaded()) {
        // Even if all denoted ViewIds were present in the client's state when entering the loop, we
        // may find any element to be missing from the respository.
        // 
        // Suppose we are processing the i-th View. View(i-1) was not loaded, thus we proceeded to
        // load its model, then the resulting ViewContextChange prescribed us to close View(i).
        // Recursing into `onViewContextChange` we correctly closed View(i), sent a
        // ViewContextUpdate to the application which resulted in no further closing-opening of
        // Views.
        //
        // Successfully completing the above would unwind execution eventually to this exact point,
        // where we look up View(i) and would not find it. This is not an error, but an expectation. 
        continue;
      }

      final var change = api
          .component(interactionContext.push("Loading ComponentModel of " + view))
          .getComponentModel2(id);
      onViewContextChange(interactionContext, change);
    }
  }

  public void performAction(String interaction, ViewId viewId, UiActionRequest request) {
    final var view = repository.get(viewId);
    final var ctx = new InteractionContext(interaction);
    final var change = api
        .component(ctx.push("Performing Action [ " + request.getCode() + " ] on " + view))
        .performAction(viewId, request);
    onViewContextChange(ctx, change);
    System.out.println(ctx);
  }

  public void performAction(String interaction, ViewId viewId, WidgetId widgetId,
                            NodeId nodeId, UiActionRequest request) {
    final var view = repository.get(viewId);

  }

  @Override
  public ViewHandle view(String name) {
    return new ViewHandle(() -> repository.find(name)
        .map(ComponentLocationResult::some)
        .orElseGet(missingView("name", name)));
  }

  @Override
  public ViewHandle view(UUID uuid) {
    return new ViewHandle(() -> Optional.ofNullable(repository.get(new ViewId(uuid)))
        .map(ComponentLocationResult::some)
        .orElseGet(missingView("uuid", uuid)));
  }

  private Supplier<ComponentLocationResult<ClientView>> missingView(String qualifier,
                                                                    Object descriptor) {
    return () -> ComponentLocationResult.none(
        "View [ " + qualifier + ": " + descriptor + " ]",
        "View [ " + qualifier + ": " + descriptor
        + " ] was not present among the open views. Open views were:\n"
        + repository.report());
  }

}
