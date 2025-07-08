package br.com.styli.domain.service;

import br.com.styli.domain.model.Comentario;
import br.com.styli.domain.model.Empresa;
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
}
