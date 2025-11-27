package com.aestallon.smartbit4all.mock.client.core.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.smartbit4all.api.localauthentication.bean.LocalAuthenticationLoginRequest;
import com.aestallon.smartbit4all.mock.client.core.TestClient;
import com.google.errorprone.annotations.Var;

public class TestClientBuilder {

  static BasePathSpec builder() {
    return new TestClientBuilder().new BasePathSpec();
  }

  // subclasses like LocalAuth with username, password strings
  public sealed interface ImplicitAuthConfigurer {}


  // example ImplicitAuthConfigurer subclass
  public record LocalAuth(String username, String password) implements ImplicitAuthConfigurer {}


  // this should define on INSTEAD_OF_ACTION and BEFORE_ACTION login handling how to extract the
  // credentials from the view's model to where the ActionSelector points and WHAT endpoint to
  // invoke with the credentials.
  // This should be basically a mapper
  // from viewModel.username --> payload.username
  // from viewModel.password --> payload.password
  // from viewModel.specialField --> header.X-Special-Field
  // and a LaunchCallConfigurer-like thingy that defines the endpoint and method to invoke.
  public interface CredentialExtractor {}


  // lets you define an action by specifying ViewName -> ActionCode
  // the action is added as an implicit component by default
  public interface ActionSelector {}


  // lets you define a launch call to perform (endpoint, method, maybe body/parameters)
  public interface LaunchCallConfigurer {}


  // something like this should be a descendant of BootstrapAction.
  // each bootstrap action should define its own interaction context? or only a single one for the bootstrap itself?
  // LaunchCallConfigurer should produce a LaunchCall.
  // SmartLink is a simple LaunchCall, which goes through the View API with the provided string
  interface LaunchCall {

    void execute(MockClient client);

  }


  static void foo() {
    TestClientBuilder.builder()
        .withDefaultBasePaths()
        .withImplicitAuthentication(new LocalAuth("username", "password"))
        .withLaunchCall(new LaunchCallConfigurer() {})
        .build();
    TestClientBuilder.builder()
        .withDefaultBasePaths()
        .withNoAuthentication()
        .withSmartLink("/foo/bar")
        .build();
    TestClientBuilder.builder()
        .withDefaultBasePaths()
        .withExplicitAuthentication()
        .withLoginPerformedBeforeAction(new ActionSelector() {})
        .withLaunchCall(new LaunchCallConfigurer() {});
    TestClientBuilder.builder()
        .withDefaultBasePaths()
        .withExplicitAuthentication()
        .withLoginPerformedInsteadOfAction(new ActionSelector() {})
        .withDedicatedAnonymousAndAuthenticatedLaunchCalls(
            new LaunchCallConfigurer() {},
            new LaunchCallConfigurer() {})
        .build();
  }


  private static final String BASE_PATH_DEFAULT = "/api";

  private String viewApiBasePath = BASE_PATH_DEFAULT;
  private String gridApiBasePath = BASE_PATH_DEFAULT;
  private String localAuthBasePath = BASE_PATH_DEFAULT;
  private String sessionBasePath = BASE_PATH_DEFAULT;

  @Deprecated
  private BiConsumer<MockClient, InteractionContext> authenticator;


  private enum LoginHandlerType { SERVER_SIDE, INSTEAD_OF_ACTION, BEFORE_ACTION }

  // -----------------------------------------------------------------------------------------------
  // This is the net information the builder needs to assemble a sequence of boostrap actions.
  private boolean isAuthConfigured;
  private boolean isAuthImplicit;
  private LaunchCall genericOrSmartlinkLaunchCall;
  private ActionSelector loginAction;
  private LoginHandlerType loginHandlerType;
  private CredentialExtractor credentialExtractor;
  // TODO: Implicit Component Registry
  // -----------------------------------------------------------------------------------------------


  public MockClient build() {
    return null;
  }

  public static final class BasePathConfigurer {

    public static void withDefaults(BasePathConfigurer configurer) {
      // no op
    }

    private static String check(String basePath) {
      if (basePath == null || basePath.isBlank()) {
        throw new IllegalArgumentException("Base path '" + basePath + "' cannot be null or blank!");
      }

      if (!basePath.startsWith("/")) {
        throw new IllegalArgumentException("Base path '" + basePath + "' must start with '/'!");
      }

      if (basePath.endsWith("/")) {
        throw new IllegalArgumentException("Base path '" + basePath + "' cannot end with '/'!");
      }

      if (basePath.contains("//")) {
        throw new IllegalArgumentException("Base path '" + basePath + "' cannot contain '//'!");
      }

      try {
        new URI("any:" + basePath);
      } catch (final URISyntaxException e) {
        throw new IllegalArgumentException("Base path '" + basePath + "' must be URI-safe!", e);
      }

      return basePath;
    }

    private String viewApiBasePath = BASE_PATH_DEFAULT;
    private String gridApiBasePath = BASE_PATH_DEFAULT;
    private String localAuthBasePath = BASE_PATH_DEFAULT;
    private String sessionBasePath = BASE_PATH_DEFAULT;

    public BasePathConfigurer view(String basePath) {
      this.viewApiBasePath = check(basePath);
      return this;
    }

    public BasePathConfigurer grid(String basePath) {
      this.gridApiBasePath = check(basePath);
      return this;
    }

    public BasePathConfigurer localAuth(String basePath) {
      this.localAuthBasePath = check(basePath);
      return this;
    }

    public BasePathConfigurer session(String basePath) {
      this.sessionBasePath = check(basePath);
      return this;
    }

  }


  public abstract static class TestClientBuilderSpec<SELF extends TestClientBuilderSpec<SELF>> {

    protected abstract SELF self();

  }


  public final class BasePathSpec extends TestClientBuilderSpec<BasePathSpec> {

    @Override
    protected BasePathSpec self() {
      return this;
    }

    public AuthSpec withBasePaths(Consumer<BasePathConfigurer> configuration) {
      final var configurer = new BasePathConfigurer();
      configuration.accept(configurer);
      viewApiBasePath = configurer.viewApiBasePath;
      gridApiBasePath = configurer.gridApiBasePath;
      localAuthBasePath = configurer.localAuthBasePath;
      sessionBasePath = configurer.sessionBasePath;
      return new AuthSpec();
    }

    public AuthSpec withDefaultBasePaths() {
      return withBasePaths(BasePathConfigurer::withDefaults);
    }

  }


  public final class AuthSpec extends TestClientBuilderSpec<AuthSpec> {
    @Override
    protected AuthSpec self() {
      return this;
    }

    public NoOrImplicitAuthLaunchSpec withImplicitAuthentication(
        ImplicitAuthConfigurer configurer) {
      if (configurer instanceof LocalAuth(var username, var password)) {
        authenticator = (client, ctx) -> client.api()
            .localAuth(ctx)
            .login(new LocalAuthenticationLoginRequest()
                .username(username)
                .password(password));
      }
      return new NoOrImplicitAuthLaunchSpec();
    }

    public NoOrImplicitAuthLaunchSpec withNoAuthentication() {
      return new NoOrImplicitAuthLaunchSpec();
    }

    public LoginHandlerSpec withExplicitAuthentication() {
      return new LoginHandlerSpec();
    }

  }


  public abstract sealed class LaunchSpec<T extends LaunchSpec<T>>
      extends TestClientBuilderSpec<T> {

    public ImplicitComponentRegistrySpec withLaunchCall(LaunchCallConfigurer configurer) {
      return new ImplicitComponentRegistrySpec();
    }

    public ImplicitComponentRegistrySpec withSmartLink(String smartLink) {
      return new ImplicitComponentRegistrySpec();
    }

  }


  public final class NoOrImplicitAuthLaunchSpec extends LaunchSpec<NoOrImplicitAuthLaunchSpec> {

    @Override
    protected NoOrImplicitAuthLaunchSpec self() {
      return this;
    }

  }


  public final class InsteadOfActionLaunchSpec extends LaunchSpec<InsteadOfActionLaunchSpec> {

    @Override
    protected InsteadOfActionLaunchSpec self() {
      return this;
    }

    public ImplicitComponentRegistrySpec withDedicatedAnonymousAndAuthenticatedLaunchCalls(
        LaunchCallConfigurer anonymousAction, LaunchCallConfigurer authenticatedAction) {
      return new ImplicitComponentRegistrySpec();
    }

  }


  public final class LoginHandlerSpec extends TestClientBuilderSpec<LoginHandlerSpec> {

    @Override
    protected LoginHandlerSpec self() {
      return this;
    }

    public NoOrImplicitAuthLaunchSpec withLoginPerformedBeforeAction(ActionSelector selector) {
      return new NoOrImplicitAuthLaunchSpec();
    }

    public InsteadOfActionLaunchSpec withLoginPerformedInsteadOfAction(ActionSelector selector) {
      return new InsteadOfActionLaunchSpec();
    }

    public ImplicitComponentRegistrySpec withLoginPerformedServerSide() {
      return new ImplicitComponentRegistrySpec();
    }

  }


  public final class ImplicitComponentRegistrySpec
      extends TestClientBuilderSpec<ImplicitComponentRegistrySpec> {

    @Override
    protected ImplicitComponentRegistrySpec self() {
      return this;
    }

    public ReadyTestClient build() {
      return new ReadyTestClient(null, Collections.emptyList());
    }

  }


}
