package br.com.styli.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtService {

    // Em produção: use um segredo forte fora do código e do git
    private static final String SECRET = "b2a9e2d1c0b9a8e7d6c5b4a3f2e1d0c9b8a7f6e5d4c3b2a19080706050403021";

    // 30 minutos
    private static final long ACCESS_EXP_SECONDS = 30 * 60;
    // 7 dias
    private static final long REFRESH_EXP_SECONDS = 7 * 24 * 60 * 60;

    private Key getKey() {
        // mantém compatível com seu código atual
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(Base64.getEncoder().encodeToString(SECRET.getBytes()))
        );
    }

    // ========= GERAR TOKENS =========

    /**
     * Gera access token SEM claims extras (apenas roles padrão).
     */
    public String generateAccessToken(UserDetails user) {
        return generateAccessToken(user, Map.of());
    }

    /**
     * Gera access token COM claims extras (ex.: clienteId, funcionarioId).
     */
    public String generateAccessToken(UserDetails user, Map<String, Object> extraClaims) {
        Instant now = Instant.now();

        // Roles em formato "ROLE_X,ROLE_Y"
        String roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        if (extraClaims != null && !extraClaims.isEmpty()) {
            claims.putAll(extraClaims);
        }

        return Jwts.builder()
                .setSubject(user.getUsername())
                .addClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(ACCESS_EXP_SECONDS)))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Gera refresh token (sem claims customizadas).
     */
    public String generateRefreshToken(UserDetails user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(REFRESH_EXP_SECONDS)))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ========= LEITURA/VALIDAÇÃO =========

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

    // ========= GETTERS DE CLAIMS CUSTOMIZADAS =========

    public Optional<Long> getClienteId(String token) {
        try {
            Object v = parse(token).getBody().get("clienteId");
            if (v == null) return Optional.empty();
            if (v instanceof Integer i) return Optional.of(i.longValue());
            if (v instanceof Long l) return Optional.of(l);
            if (v instanceof String s) return Optional.of(Long.parseLong(s));
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Long> getFuncionarioId(String token) {
        try {
            Object v = parse(token).getBody().get("funcionarioId");
            if (v == null) return Optional.empty();
            if (v instanceof Integer i) return Optional.of(i.longValue());
            if (v instanceof Long l) return Optional.of(l);
            if (v instanceof String s) return Optional.of(Long.parseLong(s));
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Retorna roles do token como conjunto (ex.: ["ROLE_CLIENTE","ROLE_ADMIN"])
     */
    public Set<String> getRoles(String token) {
        try {
            Object v = parse(token).getBody().get("roles");
            if (v == null) return Set.of();
            String s = String.valueOf(v);
            if (s.isBlank()) return Set.of();
            return new HashSet<>(Arrays.asList(s.split(",")));
        } catch (Exception e) {
            return Set.of();
        }
    }
}
