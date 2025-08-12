package br.com.styli.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_servico_id")
    private EmpresaServico servico;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario; // Pode ser null se for aleatório

    private LocalDateTime inicio;
    private LocalDateTime fim;

    private String observacoes;

    private String motivoCancelamento;
    private LocalDateTime canceladoEm;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;
}
