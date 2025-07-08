package br.com.styli.domain.usecase;

import br.com.styli.domain.model.Agendamento;
import br.com.styli.domain.model.HorarioAtendimentoFuncionario;
import br.com.styli.domain.repository.HorarioAtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HorarioAtendimentoFuncionarioUseCase {

    @Autowired
    HorarioAtendimentoRepository horarioAtendimentoRepository;

    public List<HorarioAtendimentoFuncionario> findAll(){
        List<HorarioAtendimentoFuncionario> horarioList = horarioAtendimentoRepository.findAll();
        return horarioList;
    }
}
