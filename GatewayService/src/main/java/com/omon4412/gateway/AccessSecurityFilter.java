package com.omon4412.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class AccessSecurityFilter extends AbstractGatewayFilterFactory<AccessSecurityFilter.Config> {
    private final WebClient.Builder webClientBuilder;

    @Value("${authservice.url}")
    private String authServiceUrl;

    public AccessSecurityFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            HttpCookie sessionCookieValue = exchange.getRequest().getCookies().getFirst("SESSION");
            if (sessionCookieValue == null) {
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
            } else {
                String requestPath = exchange.getRequest().getPath().toString();
                return webClientBuilder.build()
                        .get()
                        .uri(authServiceUrl + requestPath)
                        .header(HttpHeaders.COOKIE, sessionCookieValue.toString())
                        .exchange()
                        .flatMap(response -> {
                            if (response.statusCode().equals(HttpStatus.NOT_FOUND) || response.statusCode().equals(HttpStatus.OK)) {
                                return chain.filter(exchange);
                            } else {
                                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized)))"));
                            }
                        });
            }
        };
    }

    public static class Config {
    }
}
