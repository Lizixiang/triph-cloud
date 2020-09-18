package tripleh.gateway.com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Author: zixli
 * Date: 2020/9/16 19:12
 * FileName: GateWayWebSecurityConfig
 * Description: 安全认证
 */
@Configuration
@EnableWebFluxSecurity
public class GateWayWebSecurityConfig {

    private static final String MAX_AGE = "18000L";

    @Autowired
    private PermissionAuthorizationManager permissionAuthorizationManager;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    public SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) {
        /**
         * 验证顺序： 1.认证header传过来的token
         *           2.对登录的用户进行权限验证
         *           3.网关过滤器往header中设置SECURITY_KEY，用于验证是否经过网关调用其他服务
         */
        // token管理器
        ReactiveRedisAuthenticationManager reactiveRedisAuthenticationManager = new ReactiveRedisAuthenticationManager(new RedisTokenStore(redisConnectionFactory));
        // 认证管理器
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveRedisAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());
        http.httpBasic().disable()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
//                .pathMatchers("/triph-auth/**").permitAll()
                .anyExchange().access(permissionAuthorizationManager)
                .and()
                .addFilterAt(corsFilter(), SecurityWebFiltersOrder.CORS) // 跨域
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION); // oauth2认证过滤器
//        http.oauth2ResourceServer().jwt();
        return http.build();
    }

    /**
     * 跨域配置
     */
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                HttpHeaders requestHeaders = request.getHeaders();
                ServerHttpResponse response = ctx.getResponse();
                HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
                HttpHeaders headers = response.getHeaders();
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
                headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
                if (requestMethod != null) {
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
                }
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
                headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }

}
