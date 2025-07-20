package br.com.styli.domain.usecase;

import br.com.styli.domain.dto.response.AgendamentoResponse;
import br.com.styli.domain.mapper.AgendamentoMapper;
import br.com.styli.domain.model.Agendamento;
import br.com.styli.domain.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AgendamentoUseCase {

    @Autowired
    AgendamentoRepository agendamentoRepository;

    public List<AgendamentoResponse> findAll(){
        List<Agendamento> agendamentoList = agendamentoRepository.findAll();

        List<AgendamentoResponse> agendamentoResponse = new ArrayList<>();

        for(Agendamento agendamento: agendamentoList){
            AgendamentoResponse response = AgendamentoMapper.toResponse(agendamento);
            agendamentoResponse.add(response);
        }

        return agendamentoResponse;
    }
}
