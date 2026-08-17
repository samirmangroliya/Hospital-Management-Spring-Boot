package com.hospital.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log =
            LoggerFactory.getLogger(RequestLoggingFilter.class);

    private static final String CORRELATION_ID = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        String correlationId =
                request.getHeaders().getFirst(CORRELATION_ID);

        String method = request.getMethod() != null
                ? request.getMethod().name()
                : "UNKNOWN";

        String path = request.getURI().getPath();

        long startTime = System.currentTimeMillis();

        log.info(
                "REQUEST correlationId={} method={} path={}",
                correlationId,
                method,
                path
        );

        return chain.filter(exchange)
                .doFinally(signalType -> {

                    long duration =
                            System.currentTimeMillis() - startTime;

                    Integer statusCode =
                            exchange.getResponse()
                                    .getStatusCode() != null
                                    ? exchange.getResponse()
                                    .getStatusCode().value()
                                    : null;

                    log.info(
                            "RESPONSE correlationId={} status={} durationMs={}",
                            correlationId,
                            statusCode,
                            duration
                    );
                });
    }

    @Override
    public int getOrder() {
        return -90;
    }
}