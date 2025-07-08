package br.com.styli.domain.usecase;

import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class EmpresaUseCase {

    @Autowired
    EmpresaRepository empresaRepository;

    public List<Empresa> findAll(){
        List<Empresa> empresaList = empresaRepository.findAll();
        return empresaList ;
    }
}
