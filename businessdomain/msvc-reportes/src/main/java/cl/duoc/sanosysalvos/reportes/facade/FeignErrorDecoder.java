package cl.duoc.sanosysalvos.reportes.facade;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if (response.body() != null) {
                String body = Util.toString(response.body().asReader(Util.UTF_8));
                if (!body.isBlank()) {
                    return new RuntimeException(body);
                }
            }
        } catch (IOException ignored) {
        }
        return defaultDecoder.decode(methodKey, response);
    }
}
