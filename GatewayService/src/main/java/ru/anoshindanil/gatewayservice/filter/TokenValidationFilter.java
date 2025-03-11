package ru.anoshindanil.gatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
public class TokenValidationFilter extends AbstractGatewayFilterFactory<TokenValidationFilter.Config> {

    public TokenValidationFilter () {
        super(TokenValidationFilter.Config.class);
    }

    private SecretKey key =null;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
//
//        if (token == null || !token.startsWith("Bearer ")) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//        }
//
//            try {
//                Claims claims = Jwts.parser()
//                        .verifyWith(key)
//                        .build()
//                        .parseSignedClaims(token)
//                        .getPayload();
//            } catch (JwtException | IllegalArgumentException e) {
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                return exchange.getResponse().setComplete();
//            }
//
//            return chain.filter(exchange);
//    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange,chain)-> {
            String string = exchange.getRequest().getPath().toString();
            System.out.println(string);
            if (string.equals("/auth/register")) {
                return chain.filter(exchange);
            }
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            }
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
            } catch (JwtException | IllegalArgumentException e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }
    public static class Config {

    }
}
