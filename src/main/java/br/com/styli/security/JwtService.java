package br.com.styli.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {

    // Gere algo forte e guarde fora do git em prod; aqui é simples p/ dev
    private static final String SECRET = "b2a9e2d1c0b9a8e7d6c5b4a3f2e1d0c9b8a7f6e5d4c3b2a19080706050403021";

    // 30 minutos
    private static final long ACCESS_EXP_SECONDS = 30 * 60;
    // 7 dias
    private static final long REFRESH_EXP_SECONDS = 7 * 24 * 60 * 60;

    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(SECRET.getBytes())));
    }

    public String generateAccessToken(UserDetails user) {
        Instant now = Instant.now();
        String roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return Jwts.builder()
                .setSubject(user.getUsername())
                .addClaims(Map.of("roles", roles))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(ACCESS_EXP_SECONDS)))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(REFRESH_EXP_SECONDS)))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return parse(token).getBody().getSubject();
    }

    public boolean isValid(String token, UserDetails user) {
        try {
            Claims c = parse(token).getBody();
            return c.getSubject().equals(user.getUsername()) && c.getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isExpired(String token) {
        try {
            return parse(token).getBody().getExpiration().before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
    }
}
