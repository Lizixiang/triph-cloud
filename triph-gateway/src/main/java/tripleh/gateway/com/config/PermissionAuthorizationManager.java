package tripleh.gateway.com.config;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Author: zixli
 * Date: 2020/9/16 19:13
 * FileName: PermissionAuthorizationManager
 * Description: 权限管理器
 */
@Slf4j
@Component
public class PermissionAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private Set<String> permitAll = Sets.newConcurrentHashSet();
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();


    public PermissionAuthorizationManager (){
        permitAll.add("/");
        permitAll.add("/error");
        permitAll.add("/favicon.ico");
        permitAll.add("/**/v2/api-docs/**");
        permitAll.add("/**/swagger-resources/**");
        permitAll.add("/webjars/**");
        permitAll.add("/doc.html");
        permitAll.add("/swagger-ui.html");
        permitAll.add("/**/oauth/**");
    }

    /**
     * 实现权限验证判断
     */
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authenticationMono, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();
        // 请求资源
        String requestPath = exchange.getRequest().getURI().getPath();
        // 是否直接放行
        if (permitAll(requestPath)) {
            Mono<AuthorizationDecision> just = Mono.just(new AuthorizationDecision(true));
            return just;
        }
        Mono<AuthorizationDecision> authorizationDecisionMono = authenticationMono
                .map(auth -> new AuthorizationDecision(checkAuthorities(exchange, auth, requestPath)))
                .defaultIfEmpty(new AuthorizationDecision(false));
        return authorizationDecisionMono;
    }

    /**
     * 校验是否属于静态资源
     * @param requestPath 请求路径
     * @return
     */
    private boolean permitAll(String requestPath) {
        return permitAll.stream()
                .filter(r -> antPathMatcher.match(r, requestPath)).findFirst().isPresent();
    }

    //权限校验
    private boolean checkAuthorities(ServerWebExchange exchange, Authentication auth, String requestPath) {
        if(auth instanceof OAuth2Authentication){
            OAuth2Authentication athentication = (OAuth2Authentication) auth;
            String clientId = athentication.getOAuth2Request().getClientId();
            log.info("clientId is {}",clientId);
        }
        // TODO: 2020/9/17 自定义权限验证
        Object principal = auth.getPrincipal();
        log.info("用户信息:{}",principal.toString());
        return true;
    }

}