package br.com.styli.domain.dto.response;

import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
public class HorarioAtendimentoFuncResponse {
    private DayOfWeek diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private long idFuncionario;

}
