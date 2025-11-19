package com.aestallon.smartbit4all.mock.client.core.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.smartbit4all.api.localauthentication.bean.LocalAuthenticationLoginRequest;

public class TestClientBuilder {

  static BasePathSpec builder() {
    return new TestClientBuilder().new BasePathSpec();
  }
  
  static void foo() {
    builder()
        .withBasePaths(BasePathConfigurer::withDefaults)
        .withNoAuthentication()
        .withSmartLink("/asdasd")
        .withNoImplicitComponents()
        .build();
  }

  private static final String BASE_PATH_DEFAULT = "/api";


  sealed interface AuthExec {

    record Never() implements AuthExec {}


    record BeforeViewContextInit() implements AuthExec {}


    record AfterViewContextInit() implements AuthExec {}


    record AsAction(String viewName, String actionCode) implements AuthExec {}


    record BeforeAction(String viewName, String actionCode) implements AuthExec {}

  }


  private String viewApiBasePath = BASE_PATH_DEFAULT;
  private String gridApiBasePath = BASE_PATH_DEFAULT;
  private String localAuthBasePath = BASE_PATH_DEFAULT;
  private String sessionBasePath = BASE_PATH_DEFAULT;

  private BiConsumer<MockClient, InteractionContext> authenticator;
  private AuthExec authExec;


  /*
   * MockClient.local(ctx)
   *   .withBasePaths(BasePathConfigurer::default)
   *   .withLocalAuthentication("username", "password")
   *   .withViewContextInitAfterAuth()
   *   .withLaunchCall(it -> it.method(GET).endpoint("/home/start/{id}").withArg(it.viewContextId()))
   *   .build();
   *
   * MockClient.local(ctx)
   *   .withBasePaths(it -> it.view("/foo"))
   *   .withLocalAuthentication("username", "password")
   *   .withViewContextInitBeforeAuth()
   *   .withLaunchCall
   */

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
  }


  public final class AuthSpec extends TestClientBuilderSpec<AuthSpec> {
    @Override
    protected AuthSpec self() {
      return this;
    }

    public AuthCapableViewContextInitSpec withLocalAuthentication(String username,
                                                                  String password) {
      authenticator = (client, ctx) -> client.api()
          .localAuth(ctx)
          .login(new LocalAuthenticationLoginRequest()
              .username(username)
              .password(password));
      return new AuthCapableViewContextInitSpec();
    }

    public AnonymousViewContextInitSpec withNoAuthentication() {
      authExec = new AuthExec.Never();
      return new AnonymousViewContextInitSpec();
    }

  }


  public abstract static class ViewContextInitSpec<SELF extends ViewContextInitSpec<SELF>>
      extends TestClientBuilderSpec<SELF> {

  }


  public final class AnonymousViewContextInitSpec
      extends ViewContextInitSpec<AnonymousViewContextInitSpec> {
    @Override
    protected AnonymousViewContextInitSpec self() {
      return this;
    }

    // after session and VC has been established, the endpoint is invoked with no auth
    public ImplicitComponentSpec withLaunchCall(String endpoint) {
      // TODO: Define API Call Configurer
      return new ImplicitComponentSpec();
    }

    // after session and VC has been established, the SM endpoint is invoked with no auth
    public ImplicitComponentSpec withSmartLink(String smartLinkPath) {
      // TODO: Save SmartLink and define launch-by-smart-link
      return new ImplicitComponentSpec();
    }

  }


  public final class AuthCapableViewContextInitSpec
      extends ViewContextInitSpec<AuthCapableViewContextInitSpec> {
    @Override
    protected AuthCapableViewContextInitSpec self() {
      return this;
    }
    
    // session, auth, VC, then we invoke the SM endpoint
    public ImplicitComponentSpec withSmartLink(String smartLinkPath) {
      // TODO: Save SmartLink and define launch-by-smart-link
      authExec = new AuthExec.BeforeViewContextInit();
      return new ImplicitComponentSpec();
    }
  }


  public final class ImplicitComponentSpec extends TestClientBuilderSpec<ImplicitComponentSpec> {

    @Override
    protected ImplicitComponentSpec self() {
      return this;
    }


    public TestClientBuilder withNoImplicitComponents() {
      return TestClientBuilder.this;
    }

  }
}
