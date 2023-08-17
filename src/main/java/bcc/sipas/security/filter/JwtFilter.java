package bcc.sipas.security.filter;

import bcc.sipas.constant.SecurityConstant;
import bcc.sipas.exception.UnauthorizedException;
import bcc.sipas.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class JwtFilter extends AuthenticationWebFilter {

    @Autowired
    @Qualifier("exceptionHandlerMethodResolver")
    private ExceptionHandlerMethodResolver exceptionHandlerMethodResolver;

    public JwtFilter(ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var req = exchange.getRequest();
        return Mono.defer(() -> {
            String token = req.getHeaders()
                    .toSingleValueMap()
                    .get(SecurityConstant.AUTHORIZATION);
            if(StringUtils.hasText(token) && JwtUtil.validateToken(token)){
                return Mono.fromRunnable(() -> {
                    Authentication auth = JwtUtil.getAuthentication(token);
                    if (auth.isAuthenticated()){
                        ReactiveSecurityContextHolder.getContext()
                                .contextWrite(context -> {
                                    context.put("auth", auth);
                                    return context;
                                });
                        chain.filter(exchange);
                    } else {
                        this.exceptionHandlerMethodResolver.resolveMethod(new UnauthorizedException("token tidak valid"));
                    }
                });
            } else {
                return Mono.fromRunnable(() -> {
                    this.exceptionHandlerMethodResolver.resolveMethod(new UnauthorizedException("token tidak valid"));
                });
            }
        });
    }
}
