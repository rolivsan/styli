package br.com.styli.domain.service;

import br.com.styli.domain.model.Agendamento;
import br.com.styli.domain.model.Cliente;
import br.com.styli.domain.usecase.AgendamentoUseCase;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AgendamentoService {

    @Autowired
    AgendamentoUseCase agendamentoUseCase;

    public List<Agendamento> findAll(){
        List<Agendamento> agendamentoList = agendamentoUseCase.findAll();
        return agendamentoList;
    }
}
