package com.hmall.gateway.filters;

import com.hmall.common.exception.UnauthorizedException;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter , Ordered {

    private final AuthProperties authProperties;

    private final JwtTool jwtTool;

    // excludePaths 采用了特殊语法，这是用来校验excludePaths表达式的
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 登陆校验
        // 1.获取用户信息：获取request
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        RequestPath path = request.getPath();
        // 2.判断是否需要做登录拦截（看request的请求路径与yaml文件里配置的路径是否一致
        if(isExclude(path.toString())){
            // 放行
            return chain.filter(exchange);
        }
        // 如果不在可放行的列表里，那就需要做登录校验
        // 3.获取token
        List<String> authorization = headers.get("authorization");
        String token = null;
        if (authorization != null && !authorization.isEmpty()) {
            token = authorization.get(0);
        }
        // 4.解析并校验token
        Long userId = null;
        try {
            userId = jwtTool.parseToken(token);
        }catch (UnauthorizedException e){
            // 当JwtTool里抛出异常时，我要中止请求，而不仅仅是抛出异常
            // 若不中止请求，那么最后的状态码就是500，无法得知异常的原因
            // 所以要在这里设置异常时的状态码为401
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED); // 设置当前请求返回的状态码
            return response.setComplete(); // 表示在这里中止当前请求
        }
        // 5.传递用户信息
        // 如果在这里拿到userId那就可以继续往下走,拿不到就说明在前面抛异常并中止请求了，拿得到说明到这一步都没问题
        System.out.println("userId = " + userId);
        String userInfo = userId.toString();
        ServerWebExchange swe = exchange.mutate()
                .request(builder -> builder.header("user-info",userInfo)) //请求头的key和value
                .build();
        // 6.放行
        return chain.filter(swe);
    }

    // 校验路径的方法
    private boolean isExclude(String path) {
        for (String pathPattern: authProperties.getExcludePaths()) {
            if(antPathMatcher.match(pathPattern, path)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
