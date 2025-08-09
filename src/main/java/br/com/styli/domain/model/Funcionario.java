package br.com.styli.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;
    private String telefone;

    @ElementCollection
    private List<String> imagens;

    // Empresas em que trabalha
    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private List<FuncionarioEmpresa> vinculos;

    @OneToMany(mappedBy = "funcionario", cascade = CascadeType.ALL)
    private List<FuncionarioServicoEmpresa> servicosEmpresa;
}
