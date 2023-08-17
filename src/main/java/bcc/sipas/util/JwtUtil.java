package bcc.sipas.util;

import bcc.sipas.constant.SecurityConstant;
import bcc.sipas.security.authentication.JwtAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class JwtUtil {

    private static final Key keys = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateToken(JwtAuthentication<?> authentication){
        String username = authentication.getName();
        Object id = authentication.getId();
        Optional<GrantedAuthority> role = authentication.getAuthorities()
                .stream()
                .findFirst();
        Claims claims = Jwts
                .claims()
                .setSubject(username)
                .setId(id.toString());
        role.ifPresent((v) ->  claims.put(SecurityConstant.ROLE, v));
        LocalDate now = LocalDate.now();
        return Jwts.builder()
                .addClaims(claims)
                .signWith(keys)
                .setIssuedAt(Date.from(Instant.ofEpochMilli(now.toEpochDay())))
                .compact();
    }

    public static boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(keys)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch(Exception ex){
            return false;
        }
    }

    public static Authentication getAuthentication(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(keys)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Object auth = claims.get(SecurityConstant.ROLE);
        List<GrantedAuthority> list = auth == null ?
                List.of()
                :
                Arrays.stream(((String)auth).split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        var jwtAuth = list.isEmpty()?
                new JwtAuthentication<Object>(claims.getSubject(), null)
                :
                new JwtAuthentication<Object>(claims.getSubject(), null, list);
        jwtAuth.setId(claims.getId());
        return jwtAuth;
    }
}
