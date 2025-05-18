package org.metalib.market.data.ibkr.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.metalib.market.data.ibkr.api.LogoutApi;
import org.metalib.market.data.ibkr.model.LogoutPost200Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class LogoutHttp implements LogoutApi {


    final String baseUrl;
    final HttpClient httpClient;
    final ObjectMapper jackson;

    @Override
    public LogoutPost200Response logoutPost() {
        try {
            final var response = httpClient
                    .send(HttpRequest
                                    .newBuilder().uri(UriBuilder.fromUri(baseUrl).path("/logout").build())
                                    .POST(HttpRequest.BodyPublishers.noBody()).build(),
                            new JacksonBodyHandler<LogoutPost200Response>(jackson));
            if (200 == response.statusCode()) {
                return response.body().get();
            }
            return null;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @RequiredArgsConstructor
    static class JacksonBodyHandler<W> implements HttpResponse.BodyHandler<Supplier<W>> {

        final ObjectMapper jackson;

        private <W> Supplier<W> toSupplierOfType(InputStream inputStream) {
            return () -> {
                try (InputStream stream = inputStream) {
                    return jackson.readValue(stream, new TypeReference<W>() {});
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            };
        }

        private <W> HttpResponse.BodySubscriber<Supplier<W>> asJSON() {
            return HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofInputStream(), this::toSupplierOfType);
        }

        @Override
        public HttpResponse.BodySubscriber<Supplier<W>> apply(HttpResponse.ResponseInfo responseInfo) {
            return asJSON();
        }

    }
}
