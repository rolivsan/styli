package br.com.styli.domain.service;

import br.com.styli.domain.dto.response.FuncionarioResponse;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.usecase.FuncionarioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {
    @Autowired
    FuncionarioUseCase funcionarioUseCase;

    public List<FuncionarioResponse> findAll(Long idEmpresa){
        List<FuncionarioResponse> funcionarioList = funcionarioUseCase.findAll(idEmpresa);
        return funcionarioList;
    }

    public FuncionarioResponse create(Funcionario funcionario){
        FuncionarioResponse funcionario1 = funcionarioUseCase.save(funcionario);
        return funcionario1;
    }
}
