package tripleh.gateway.com.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import reactor.core.publisher.Mono;
import tripleh.gateway.com.handler.ServiceException;

/**
 * Author: zixli
 * Date: 2020/9/16 20:41
 * FileName: ReactiveRedisAuthenticationManager
 * Description: 自定义认证接口管理类
 */
@Slf4j
public class ReactiveRedisAuthenticationManager implements ReactiveAuthenticationManager {

    private TokenStore tokenStore;

    public ReactiveRedisAuthenticationManager(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(a -> a instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class)
                .map(BearerTokenAuthenticationToken::getToken)
                .flatMap((accessToken -> {
                    log.info("accessToken is :{}", accessToken);
                    OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(accessToken);
                    //根据access_token从redis获取不到OAuth2AccessToken
                    if (oAuth2AccessToken == null) {
                        throw new ServiceException("invalid access token,please check");
//                        return Mono.error(new InvalidTokenException("invalid access token,please check"));
                    } else if (oAuth2AccessToken.isExpired()) {
                        throw new ServiceException("access token has expired,please reacquire token");
//                        return Mono.error(new InvalidTokenException("access token has expired,please reacquire token"));
                    }
                    OAuth2Authentication oAuth2Authentication = this.tokenStore.readAuthentication(accessToken);
                    if (oAuth2Authentication == null) {
                        throw new ServiceException("Access Token 无效!");
//                        return Mono.error(new InvalidTokenException("Access Token 无效!"));
                    } else {
                        return Mono.just(oAuth2Authentication);
                    }
                })).cast(Authentication.class);
    }
}
