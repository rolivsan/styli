package br.com.styli.domain.mapper;

import br.com.styli.domain.dto.response.HorarioAtendimentoFuncResponse;
import br.com.styli.domain.model.HorarioAtendimentoFuncionario;

public class HorarioAtendimentoFuncMapper {

    public static HorarioAtendimentoFuncResponse toResponse (HorarioAtendimentoFuncionario horarioAtendimentoFuncionario){
        return HorarioAtendimentoFuncResponse.builder()
                .idFuncionario(horarioAtendimentoFuncionario.getFuncionario() != null ? horarioAtendimentoFuncionario.getFuncionario().getId() : null)
                .horaInicio(horarioAtendimentoFuncionario.getHoraInicio())
                .horaFim(horarioAtendimentoFuncionario.getHoraFim())
                .diaSemana(horarioAtendimentoFuncionario.getDiaSemana())
                .build();
    }
}
