package br.com.styli.domain.service;

import br.com.styli.domain.dto.response.AgendamentoResponse;
import br.com.styli.domain.usecase.AgendamentoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    AgendamentoUseCase agendamentoUseCase;

    public List<AgendamentoResponse> findAll(){
        List<AgendamentoResponse> agendamentoList = agendamentoUseCase.findAll();
        return agendamentoList;
    }
}
