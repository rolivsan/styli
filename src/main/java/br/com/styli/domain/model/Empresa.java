package br.com.styli.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Boolean destaque;

    @ElementCollection
    private List<String> imagens; // URLs das imagens

    private String instagram;

    private Double avaliacaoMedia = 0.0;
    private Integer quantidadeAvaliacoes = 0;

    @ManyToMany
    @JoinTable(
            name = "empresa_categoria",
            joinColumns = @JoinColumn(name = "empresa_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Funcionario> funcionarios;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Cliente> clientes;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Comentario> comentarios;
}

