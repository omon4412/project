package com.omon4412.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class AssignUserSecurityFilter extends AbstractGatewayFilterFactory<AssignUserSecurityFilter.Config> {
    private final WebClient.Builder webClientBuilder;

    @Value("${authservice.url}")
    private String authServiceUrl;

    public AssignUserSecurityFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            HttpCookie sessionCookieValue = exchange.getRequest().getCookies().getFirst("SESSION");
            if (sessionCookieValue == null) {
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
            }

            return webClientBuilder.build()
                    .get()
                    .uri(authServiceUrl + "/user")
                    .header(HttpHeaders.COOKIE, sessionCookieValue.toString())
                    .retrieve()
                    .bodyToMono(UserFullDto.class)
                    .flatMap(user -> {
                        ServerHttpRequest request = exchange.getRequest();
                        String originalPath = request.getPath().value();
                        String modifiedPath = originalPath.replace("/user/", "/user/" + user.getId() + "/");
                        ServerHttpRequest modifiedRequest = request.mutate().path(modifiedPath).build();
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    })
                    .onErrorResume(error -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")));
        };
    }

    public static class Config {
        // Put the configuration properties
    }
}
