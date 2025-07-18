package br.com.styli.domain.service;

import br.com.styli.domain.dto.request.FuncionarioRequest;
import br.com.styli.domain.model.Comentario;
import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.usecase.EmpresaUseCase;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EmpresaService {

    @Autowired
    EmpresaUseCase empresaUseCase;

    public List<Empresa> findAll(){
        List<Empresa> empresaList = empresaUseCase.findAll();
        return empresaList;
    }
    public Empresa findById(Long id){
        Empresa empresa = empresaUseCase.findById(id);
        return empresa;
    }

    public Empresa save(Empresa empresa){
        Empresa empresa1= empresaUseCase.save(empresa);
        return empresa1;
    }

    //TODO GUSTAVO - faltou setar os outros campos de usuario METOTO ERRADO
    public Empresa salvarFuncionario(Long empresaid, Funcionario funcionario){
        FuncionarioRequest funcionarioRequest = new FuncionarioRequest();
        Funcionario funcionario = Funcionario.builder().nomeUsuario(funcionarioRequest.getNomeUsuario()).build();
        Empresa empresa1= empresaUseCase.save(empresaid, funcionario);
        return empresa1;
    }

    public List<Empresa> findAllByCategoria(Long categoriaId) {
        return empresaUseCase.findAllByCategoria(categoriaId);
    }
}
