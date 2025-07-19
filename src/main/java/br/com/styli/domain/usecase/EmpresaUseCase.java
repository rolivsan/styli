package br.com.styli.domain.usecase;

import br.com.styli.domain.exception.BusinessException;
import br.com.styli.domain.exception.ErrorCode;
import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.repository.EmpresaRepository;
import br.com.styli.domain.repository.FuncionarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class EmpresaUseCase {

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    FuncionarioRepository funcionarioRepository;

    public List<Empresa> findAll(){
        List<Empresa> empresaList = empresaRepository.findAll();
        return empresaList ;
    }

    public Empresa findById(Long id){
        Empresa empresa = empresaRepository.findById(id).orElseThrow(() -> {
            log.error("Empresa com ID {} não encontrado",id);
            return new BusinessException(ErrorCode.EMPRESA_NOT_FOUND);
        });
        return empresa;
    }

    public Empresa findByDestaque(){
        Optional <Empresa> empresa = empresaRepository.findByDestaque(true);
        return empresa.get();
    }

    public Empresa save(Empresa empresa){
        Empresa empresaSaved = empresaRepository.save(empresa);
        return empresaSaved;
    }

    public Empresa salvarFuncioario(Long empresaid, Funcionario funcionario){
        Empresa empresa = empresaRepository
                .findById(empresaid)
                .orElseThrow(() -> {
                    log.error("Empresa com ID {} não encontrado",empresaid);
                    return new BusinessException(ErrorCode.EMPRESA_NOT_FOUND);
                });

        funcionario.setEmpresa(empresa);
        funcionarioRepository.save(funcionario);

        empresa.getFuncionarios().add(funcionario);

        return empresaRepository.save(empresa);
    }

    public List<Empresa> findAllByCategoria(Long categoriaId) {
        return empresaRepository.findByCategorias_Id(categoriaId);
    }
}