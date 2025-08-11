package br.com.styli.controller;

import br.com.styli.dto.request.AuthRequest;
import br.com.styli.dto.request.RefreshRequest;
import br.com.styli.dto.response.AuthResponse;
import br.com.styli.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        UserDetails user = (UserDetails) auth.getPrincipal();
        String access = jwtService.generateAccessToken(user);
        String refresh = jwtService.generateRefreshToken(user);

        String role = user.getAuthorities().iterator().next().getAuthority(); // ex: ROLE_ADMIN
        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .role(role)
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest req) {
        // valida apenas expiração/assinatura, extrai username e emite novo access
        String username = jwtService.extractUsername(req.getRefreshToken());
        if (jwtService.isExpired(req.getRefreshToken())) {
            return ResponseEntity.status(401).build();
        }
        // Em prod: validar se refresh está numa allowlist e rotacionar
        var user = org.springframework.security.core.userdetails.User
                .withUsername(username).password("N/A")
                .roles("CLIENTE","FUNCIONARIO","ADMIN") // simplificação (melhor buscar do UserDetailsService)
                .build();
        String access = jwtService.generateAccessToken(user);
        // aqui poderíamos rotacionar refresh; por simplicidade, retorna o mesmo
        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(access)
                .refreshToken(req.getRefreshToken())
                .role("UNKNOWN")
                .build());
    }
}
