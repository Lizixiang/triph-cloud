package tripleh.gateway.com.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tripleh.gateway.com.constant.RedisKeyConstant;
import tripleh.gateway.com.util.RedisUUID;

import java.net.URI;

/**
 * Author: zixli
 * Date: 2020/9/16 9:12
 * FileName: WebLogFilter
 * Description: 安全过滤器
 */
@Slf4j
@Data
public class SecurityGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisUUID redisUUID;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String secretKey = redisUUID.create(RedisKeyConstant.SECURITY_KEY);
        ServerHttpRequest request = exchange.getRequest().mutate().headers(httpHeaders -> {
            httpHeaders.remove(RedisKeyConstant.SECURITY_KEY);
        }).build();
        ServerHttpRequest httpRequest = request.mutate().header(RedisKeyConstant.SECURITY_KEY, secretKey).build();
        if (exchange.getRequest().getURI().toString().contains("/oauth/token")) {
            try {
                URI newUri = UriComponentsBuilder.fromUri(httpRequest.getURI())
                        .replaceQuery(httpRequest.getURI().getRawQuery()+"&grant_type=password&scope=select&client_id=client_password&client_secret=123456")
                        .build(true)
                        .toUri();

                ServerHttpRequest newRequest = httpRequest.mutate().uri(newUri).build();

                return chain.filter(exchange.mutate().request(newRequest).build());
            } catch (RuntimeException ex) {
                throw new IllegalStateException("Invalid URI query: \"" + request.getURI().getRawQuery() + "\"");
            }
        }
        return chain.filter(exchange.mutate().request(httpRequest.mutate().build()).build());
    }

    @Override
    public int getOrder() {
        return -998;
    }
}
