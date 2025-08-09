package br.com.styli.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private String cidade;
    private String uf;
    private Double latitude;
    private Double longitude;

    // Horário de funcionamento (ex: 09:00 - 18:00)
    private String horarioFuncionamento;

    // Se esta empresa aparece em destaque
    private Boolean destaque;

    // Imagens da empresa
    @ElementCollection
    private List<String> imagens;

    @ManyToMany
    @JoinTable(
            name = "empresa_categoria",
            joinColumns = @JoinColumn(name = "empresa_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<EmpresaServico> servicos;
}
