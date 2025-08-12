package br.com.styli.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorarioAtendimentoFuncionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dia da semana (MONDAY, TUESDAY, ...)
    @Enumerated(EnumType.STRING)
    private DayOfWeek diaSemana;

    // Janela de atendimento do funcionário nesse dia
    private LocalTime horaInicio;
    private LocalTime horaFim;

    // Para desativar o horário sem apagar
    private Boolean ativo = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;
}
