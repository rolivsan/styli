package br.com.styli.domain.service;

import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.usecase.EmpresaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    public Empresa salvarFuncionario(Long empresaid, Funcionario funcionario){
        return empresaUseCase.salvarFuncioario(empresaid, funcionario);
    }

    public List<Empresa> findAllByCategoria(Long categoriaId) {
        return empresaUseCase.findAllByCategoria(categoriaId);
    }
}
