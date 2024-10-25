package com.hmall.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class MyGlobalFilter implements GlobalFilter , Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 编写pre逻辑，完成登录校验
        // TODO 模拟登录校验逻辑

        // 登录凭证一般放在请求头里,请求上下文包括整个过滤器链内的共享数据
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        System.out.println("headers: " + headers);

        // 把所有过滤器共享的数据传给下一个过滤器
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
