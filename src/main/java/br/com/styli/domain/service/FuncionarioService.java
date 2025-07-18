package br.com.styli.domain.service;

import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.usecase.FuncionarioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {
    @Autowired
    FuncionarioUseCase funcionarioUseCase;

    public List<Funcionario> findAll(){
        List<Funcionario> funcionarioList = funcionarioUseCase.findAll();
        return funcionarioList;
    }

    public Funcionario save(Funcionario funcionario){
        Funcionario funcionario1 = funcionarioUseCase.save(funcionario);
        return funcionario1;
    }
}
