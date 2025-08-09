package br.com.styli.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome de login
    private String username;

    // Senha para login
    private String password;

    // Papel do usuario: ADMIN, CLIENTE ou FUNCIONARIO
    @Enumerated(EnumType.STRING)
    private Role role;
}
