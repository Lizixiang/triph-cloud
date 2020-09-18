package tripleh.gateway.com.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Author: zixli
 * Date: 2020/9/16 9:12
 * FileName: WebLogFilter
 * Description: 日志过滤器
 */
@Slf4j
public class WebLogFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("url:{}, 参数:{}, ip:{}", request.getURI(), request.getQueryParams(), request.getRemoteAddress());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -999;
    }
}
