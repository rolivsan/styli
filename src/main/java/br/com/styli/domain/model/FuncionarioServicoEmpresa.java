package br.com.styli.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioServicoEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_servico_id")
    private EmpresaServico empresaServico;

    private Integer duracaoMinutosOverride;
    private BigDecimal precoOverride;
    private Boolean ativo = true;
}
