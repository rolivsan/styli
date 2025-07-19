package br.com.styli.domain.usecase;

import br.com.styli.domain.model.HorarioAtendimentoFuncionario;
import br.com.styli.domain.repository.HorarioAtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HorarioAtendimentoFuncionarioUseCase {

    @Autowired
    HorarioAtendimentoRepository horarioAtendimentoRepository;

    public List<HorarioAtendimentoFuncionario> findAll(){
        List<HorarioAtendimentoFuncionario> horarios = horarioAtendimentoRepository.findAll();
        return horarios;
    }
}
