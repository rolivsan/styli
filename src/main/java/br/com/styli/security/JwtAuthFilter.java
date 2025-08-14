package br.com.styli.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    /**
     * Informe aqui todos os caminhos que não devem passar pelo filtro JWT.
     * Incluí tanto os defaults (/v3/api-docs, /swagger-ui) quanto os customizados (/api-docs, /swagger-ui.html).
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();

        // Preflight CORS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // Swagger/OpenAPI - defaults
        if (uri.startsWith("/v3/api-docs") || uri.startsWith("/swagger-ui")) {
            return true;
        }

        // Swagger/OpenAPI - caso tenha customizado para /api-docs e /swagger-ui.html
        if (uri.startsWith("/api-docs") || "/swagger-ui.html".equals(uri)) {
            return true;
        }

        // H2 Console (se quiser pular no filtro também)
        if (uri.startsWith("/h2-console")) {
            return true;
        }

        // Endpoints públicos de auth
        if (uri.startsWith("/auth/")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = auth.substring(7);

        try {
            String username = jwtService.extractUsername(token);
            if (username != null && !jwtService.isExpired(token)) {
                // Lê roles do claim "roles"
                var roles = jwtService.getRoles(token);
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                var authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Token inválido → não autentica; request seguirá e Security decidirá (401/403)
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }
}
