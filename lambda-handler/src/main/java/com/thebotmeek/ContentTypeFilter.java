package com.thebotmeek;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import io.micronaut.function.aws.proxy.payload1.ApiGatewayProxyServletResponse;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

// This class is a filter that removes the Content-Type header from the response as we let apigateway handle setting this header.
@Filter("/**")
public class ContentTypeFilter implements HttpServerFilter {
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        return Mono.from(chain.proceed(request)).map((MutableHttpResponse<?> resp) -> {
//            resp.getHeaders().remove("Content-Type");
            resp.contentType(MediaType.TOML_TYPE);
            System.out.println(((ApiGatewayProxyServletResponse) resp).getNativeResponse());
            return resp;
        });
    }
}
