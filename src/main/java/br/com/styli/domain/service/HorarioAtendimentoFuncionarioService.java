package br.com.styli.domain.service;

import br.com.styli.domain.model.HorarioAtendimentoFuncionario;
import br.com.styli.domain.usecase.HorarioAtendimentoFuncionarioUseCase;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HorarioAtendimentoFuncionarioService {

    @Autowired
    HorarioAtendimentoFuncionarioUseCase horarioAtendimentoFuncionarioUseCase;

    public List<HorarioAtendimentoFuncionario> findAll(){
        List<HorarioAtendimentoFuncionario> horarioList = horarioAtendimentoFuncionarioUseCase.findAll();
        return horarioList;
    }
}

