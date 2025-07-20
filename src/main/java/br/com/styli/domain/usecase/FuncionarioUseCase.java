package br.com.styli.domain.usecase;

import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class FuncionarioUseCase {

    @Autowired
    AgendamentoRepository agendamentoRepository;

    @Autowired
    HorarioAtendimentoRepository horarioAtendimentoRepository;

    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ServicoRepository servicoRepository;

    public List<Funcionario> findAll(){
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        return funcionarios ;
    }

    public Funcionario save(Funcionario funcionario){
        Funcionario funcionarioSaved = funcionarioRepository.save(funcionario);
        return funcionarioSaved;
    }
}
