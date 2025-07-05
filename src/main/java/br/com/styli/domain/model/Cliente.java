package br.com.styli.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Agendamento> agendamentos;
}

