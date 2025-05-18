package org.metalib.market.data.ibkr.http.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class JacksonBodyHandler<W> implements HttpResponse.BodyHandler<Supplier<ResponseWrapper<W>>> {
    final ObjectMapper jackson;

    @Override
    public HttpResponse.BodySubscriber<Supplier<ResponseWrapper<W>>> apply(HttpResponse.ResponseInfo info) {
        return 200 == info.statusCode() ? asBean() : asText();
    }

    private HttpResponse.BodySubscriber<Supplier<ResponseWrapper<W>>> asText() {
        return HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofByteArray(), this::toText);
    }

    private Supplier<ResponseWrapper<W>> toText(byte[] a) {
        return () -> new ResponseWrapper<>(a);
    }

    private HttpResponse.BodySubscriber<Supplier<ResponseWrapper<W>>> asBean() {
        return HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofInputStream(), this::toSupplierOfType);
    }

    private Supplier<ResponseWrapper<W>> toSupplierOfType(InputStream inputStream) {
        return () -> {
            try (final var stream = inputStream) {
                return new ResponseWrapper<>(jackson.readValue(stream, new TypeReference<W>() {
                }));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }
}
