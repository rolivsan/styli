package br.com.styli.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Builder
public class Funcionario {

    @ElementCollection
    private List<String> imagens; // URLs das imagens

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeUsuario;
    private String nomeCompleto;
    private String senha;
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToMany
    @JoinTable(
            name = "funcionario_servico",
            joinColumns = @JoinColumn(name = "funcionario_id"),
            inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    private List<Servico> servicos;

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private List<HorarioAtendimentoFuncionario> horariosAtendimento;

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private List<Agendamento> agendamentos;
}
