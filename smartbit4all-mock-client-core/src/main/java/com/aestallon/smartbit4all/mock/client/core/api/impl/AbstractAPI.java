package com.aestallon.smartbit4all.mock.client.core.api.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.aestallon.smartbit4all.mock.client.core.client.MockClient;
import com.aestallon.smartbit4all.mock.client.core.exception.ClientExceptionContext;
import com.aestallon.smartbit4all.mock.client.core.exception.NetworkExchangeException;
import com.aestallon.smartbit4all.mock.client.core.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

abstract class AbstractAPI {

  protected record UriSpec(String template, Object[] vars) {

    static UriSpec of(String template, Object var, Object... vars) {
      if (vars == null || vars.length == 0) {
        return new UriSpec(template, new Object[] { var });
      }

      final var allVars = new Object[vars.length + 1];
      allVars[0] = var;
      System.arraycopy(vars, 0, allVars, 1, vars.length);
      return new UriSpec(template, allVars);
    }

    static UriSpec of(String template) {
      return new UriSpec(template, new Object[0]);
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof UriSpec(String thatTemplate, Object[] thatVars))) {
        return false;
      }

      return Arrays.equals(vars, thatVars) && Objects.equals(template, thatTemplate);
    }

    @Override
    public int hashCode() {
      return Objects.hash(template, Arrays.hashCode(vars));
    }

    @Override
    public String toString() {
      return "UriSpec {" +
             "\n  template: " + template +
             ",\n  vars: " + StringUtil.toIndentedString(Arrays.toString(vars)) +
             "\n}";
    }
  }


  protected record RequestSpec<T>(UriSpec uriSpec, Class<T> responseType, Object body) {

    static <T> RequestSpec<T> of(UriSpec uriSpec, Class<T> responseType, Object body) {
      return new RequestSpec<>(uriSpec, responseType, body);
    }

    static <T> RequestSpec<T> of(UriSpec uriSpec, Class<T> responseType) {
      return new RequestSpec<>(uriSpec, responseType);
    }

    static RequestSpec<Void> of(UriSpec uriSpec) {
      return new RequestSpec<>(uriSpec, Void.class);
    }

    protected RequestSpec(UriSpec uriSpec, Class<T> responseType) {
      this(uriSpec, responseType, null);
    }

  }


  protected static final String HEADER_VIEW_CONTEXT_UUID = "viewContextUuid";
  protected static final String HEADER_AUTHORIZATION = "Authorization";


  private final WebTestClient client;
  private final RequestContext requestContext;

  protected AbstractAPI(WebTestClient client, RequestContext requestContext) {
    this.client = client;
    this.requestContext = requestContext;
  }

  protected final <T> T post(RequestSpec<T> requestSpec) {
    return exchange(client.post(), requestSpec);
  }

  protected final <T> T put(RequestSpec<T> requestSpec) {
    return exchange(client.put(), requestSpec);
  }

  protected final <T> T get(RequestSpec<T> requestSpec) {
    return setHeadersAndExchange(client.get().uri(
        requestContext.basePath() + requestSpec.uriSpec.template,
        requestSpec.uriSpec.vars), requestSpec);
  }

  protected final <T> T delete(RequestSpec<T> requestSpec) {
    return setHeadersAndExchange(client.delete().uri(
        requestContext.basePath() + requestSpec.uriSpec.template,
        requestSpec.uriSpec.vars), requestSpec);
  }
  

  private <T> T exchange(WebTestClient.RequestBodyUriSpec ex, RequestSpec<T> requestSpec) {
    WebTestClient.RequestBodySpec body = ex.uri(
        requestContext.basePath() + requestSpec.uriSpec.template,
        requestSpec.uriSpec.vars);

    WebTestClient.RequestHeadersSpec<?> spec;
    if (requestSpec.body != null) {
      try {
        spec = body
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(MockClient.OBJECT_MAPPER.writeValueAsString(requestSpec.body));
      } catch (JsonProcessingException e) {
        throw new NetworkExchangeException(new ClientExceptionContext(), e);
      }
    } else {
      spec = body;
    }
    return setHeadersAndExchange(spec, requestSpec);
  }

  private <T> T setHeadersAndExchange(WebTestClient.RequestHeadersSpec<?> spec,
                                      RequestSpec<T> requestSpec) {
    final var request = spec.accept(MediaType.APPLICATION_JSON).headers(headers -> {
      if (requestContext.viewContextId() != null) {
        headers.set(HEADER_VIEW_CONTEXT_UUID, requestContext.viewContextId().toString());
      } else {
        headers.remove(HEADER_VIEW_CONTEXT_UUID);
      }

      if (requestContext.token() != null) {
        headers.set(HEADER_AUTHORIZATION, "Bearer " + requestContext.token());
      } else {
        headers.remove(HEADER_AUTHORIZATION);
      }
    });
    final var response = request.exchange();
    if (requestSpec.responseType == null || Void.class.equals(requestSpec.responseType)) {
      return null;
    }

    EntityExchangeResult<byte[]> entityExchangeResult = response.expectBody().returnResult();
    HttpStatusCode status = entityExchangeResult.getStatus();
    byte[] responseBody = entityExchangeResult.getResponseBody();
    try {
      return MockClient.OBJECT_MAPPER.readValue(responseBody, requestSpec.responseType);
    } catch (IOException e) {
      throw new NetworkExchangeException(new ClientExceptionContext(), e);
    }
  }

}
