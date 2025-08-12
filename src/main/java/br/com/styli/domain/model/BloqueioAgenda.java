package br.com.styli.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BloqueioAgenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Funcionário afetado pelo bloqueio
    @ManyToOne(optional = false)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    // Intervalo bloqueado
    private LocalDateTime inicio;
    private LocalDateTime fim;

    // Opcional: marcar bloqueio do dia todo (se usar, ainda assim preencha inicio/fim do dia)
    private Boolean diaTodo = false;

    // Motivo opcional (ex.: "folga", "reunião", "manutenção")
    private String motivo;
}
