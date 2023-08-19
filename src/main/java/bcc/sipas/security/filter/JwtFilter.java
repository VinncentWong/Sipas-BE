package bcc.sipas.security.filter;

import bcc.sipas.constant.SecurityConstant;
import bcc.sipas.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Slf4j
public class JwtFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();
        String authorization = (String)req.getHeaders()
                .toSingleValueMap()
                .get(SecurityConstant.AUTHORIZATION);
        if(authorization == null){
            return chain.filter(exchange);
        }
        if(!authorization.startsWith(SecurityConstant.BEARER)){
            return chain.filter(exchange);
        }
        authorization = authorization.substring(7);
        boolean validateToken = JwtUtil.validateToken(authorization);
        if(StringUtils.hasText(authorization) && validateToken){
            Authentication jwtAuthentication = JwtUtil.getAuthentication(authorization);
            return chain.filter(exchange).contextWrite(
                    ReactiveSecurityContextHolder
                    .withAuthentication(jwtAuthentication));
        } else {
            return Mono.error(new AccessDeniedException("format token tidak valid"));
        }

    }
}
