package br.com.styli.domain.usecase;

import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class EmpresaUseCase {

    @Autowired
    EmpresaRepository empresaRepository;

    public List<Empresa> findAll(){
        List<Empresa> empresaList = empresaRepository.findAll();
        return empresaList ;
    }

    public Empresa findById(Long id){
        Optional<Empresa> empresa = empresaRepository.findById(id);
        return empresa.get();
    }

    public Empresa save(Empresa empresas){
        Empresa empresa1 = empresaRepository.save(empresas);
        return empresa1;
    }

    public Empresa salvarFuncioario(Long empresaid, Funcionario funcionario){
        Empresa empresa1= empresaRepository.save(empresaid, funcionario);
        return empresa1;
    }
}
