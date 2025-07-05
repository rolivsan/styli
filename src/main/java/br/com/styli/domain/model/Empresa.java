package br.com.styli.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String telefone;
    private String email;
    private String senha;
    private String endereco;
    private String horarioFuncionamento;
    private String logoUrl;

    @ElementCollection
    private List<String> imagens; // URLs das imagens

    private Double avaliacaoMedia = 0.0;
    private Integer quantidadeAvaliacoes = 0;

    private String instagram;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Funcionario> funcionarios;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Cliente> clientes;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Comentario> comentarios;
}

