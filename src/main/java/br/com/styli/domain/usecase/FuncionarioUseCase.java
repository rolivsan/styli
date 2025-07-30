package br.com.styli.domain.usecase;

import br.com.styli.domain.dto.response.FuncionarioResponse;
import br.com.styli.domain.exception.BusinessException;
import br.com.styli.domain.exception.ErrorCode;
import br.com.styli.domain.mapper.FuncionarioMapper;
import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FuncionarioUseCase {

    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    EmpresaRepository empresaRepository;


    public List<FuncionarioResponse> findAll(Long idEmpresa){
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new BusinessException(ErrorCode.EMPRESA_NOT_FOUND));

        List<FuncionarioResponse> funcionarios =  new ArrayList<>();

        for(Funcionario func : empresa.getFuncionarios()) {
            funcionarios.add(FuncionarioMapper.toResponse(func));
        }

        return funcionarios;
    }

    public FuncionarioResponse save(Funcionario funcionario){
        Funcionario funcionarioSaved = funcionarioRepository.save(funcionario);
        return FuncionarioMapper.toResponse(funcionarioSaved);
    }
}
