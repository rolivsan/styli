package br.com.styli.domain.mapper;

import br.com.styli.domain.dto.response.AgendamentoResponse;
import br.com.styli.domain.model.Agendamento;

public class AgendamentoMapper {

    public static AgendamentoResponse toResponse (Agendamento agendamento){
        return AgendamentoResponse.builder()
                .idAgendamento(agendamento.getId())
                .idCliente(agendamento.getCliente().getId())
                .idFuncionario(agendamento.getFuncionario() != null ? agendamento.getFuncionario().getId() : null)
                .idServico(agendamento.getServico().getId())
                .horario(agendamento.getHorario())
                .build();
    }


}
