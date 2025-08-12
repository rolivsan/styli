package br.com.styli.controller;

import br.com.styli.dto.request.AuthRequest;
import br.com.styli.dto.request.RefreshRequest;
import br.com.styli.dto.response.AuthResponse;
import br.com.styli.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        UserDetails user = (UserDetails) auth.getPrincipal();

        // Claims de exemplo (MVP): mapeando usernames para IDs fixos
        Map<String, Object> claims = buildClaimsForUser(user.getUsername());

        String access = jwtService.generateAccessToken(user, claims);
        String refresh = jwtService.generateRefreshToken(user);

        String role = user.getAuthorities().iterator().next().getAuthority(); // ex.: ROLE_CLIENTE
        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .role(role)
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshRequest req) {
        String username;
        try {
            username = jwtService.extractUsername(req.getRefreshToken());
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
        if (jwtService.isExpired(req.getRefreshToken())) {
            return ResponseEntity.status(401).build();
        }

        // Em produção: buscar usuário e papéis reais (UserDetailsService).
        // Aqui, reconstruímos o "perfil" a partir do username (mesma regra do login).
        List<String> roles = rolesForUsername(username);
        UserDetails user = User.withUsername(username)
                .password("N/A")
                .roles(roles.toArray(String[]::new)) // ex.: "CLIENTE"
                .build();

        Map<String, Object> claims = buildClaimsForUser(username);

        String access = jwtService.generateAccessToken(user, claims);
        // (poderíamos rotacionar refresh aqui; mantendo o mesmo para simplificar)
        String role = roles.isEmpty() ? "UNKNOWN" : "ROLE_" + roles.get(0);

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(access)
                .refreshToken(req.getRefreshToken())
                .role(role)
                .build());
    }

    // ========= helpers (MVP) =========

    private Map<String, Object> buildClaimsForUser(String username) {
        Map<String, Object> claims = new HashMap<>();
        switch (username) {
            case "cliente" -> claims.put("clienteId", 1L);
            case "funcionario" -> claims.put("funcionarioId", 1L);
            // admin não recebe IDs específicos
            default -> {}
        }
        return claims;
    }

    private List<String> rolesForUsername(String username) {
        return switch (username) {
            case "cliente" -> List.of("CLIENTE");
            case "funcionario" -> List.of("FUNCIONARIO");
            case "admin" -> List.of("ADMIN");
            default -> List.of("CLIENTE"); // fallback amigável
        };
    }
}
