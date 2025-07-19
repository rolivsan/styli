package br.com.styli.domain.usecase;

import br.com.styli.domain.model.Agendamento;
import br.com.styli.domain.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgendamentoUseCase {

    @Autowired
    AgendamentoRepository agendamentoRepository;

    public List<Agendamento> findAll(){
        List<Agendamento> agendamentoList = agendamentoRepository.findAll();
        return agendamentoList;
    }
}
