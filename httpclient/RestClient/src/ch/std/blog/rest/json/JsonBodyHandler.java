/**
 * Json Rest Client 
 * 
 * @author Daniel Schmutz
 * 
 * see https://www.simtech-ag.ch/blog/java/jsonrestclient
 * 
 */

package ch.std.blog.rest.json;

/**
 * see https://github.com/mjg123/java-http-clients/tree/master/src/main/java/com/twilio
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonBodyHandler<T> implements HttpResponse.BodyHandler<Supplier<T>> {

    private static final ObjectMapper om = new ObjectMapper();
    private final Class<T> targetClass;

    public JsonBodyHandler(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public HttpResponse.BodySubscriber<Supplier<T>> apply(HttpResponse.ResponseInfo responseInfo) {
        return asJSON(this.targetClass);
    }


    public static <W> HttpResponse.BodySubscriber<Supplier<W>> asJSON(Class<W> targetType) {
        HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();

        return HttpResponse.BodySubscribers.mapping(
            upstream,
            inputStream -> toSupplierOfType(inputStream, targetType));
    }

    public static <W> Supplier<W> toSupplierOfType(InputStream inputStream, Class<W> targetType) {
        return () -> {
            try (InputStream stream = inputStream) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(stream, targetType);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }
}