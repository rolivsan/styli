package br.com.styli.domain.service;

import br.com.styli.domain.dto.request.AgendamentoDinamicoRequest;
import br.com.styli.domain.dto.request.ReservarHorarioRequest;
import br.com.styli.domain.dto.response.AgendamentoResponse;
import br.com.styli.domain.dto.response.EmpresaResponse;
import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.usecase.EmpresaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    EmpresaUseCase empresaUseCase;

    public List<EmpresaResponse> findAll(){
     return empresaUseCase.findAll();
    }
    public EmpresaResponse findById(Long id){
        return empresaUseCase.findById(id);
    }

    public EmpresaResponse save(Empresa empresa){
        return empresaUseCase.save(empresa);
    }

    public EmpresaResponse salvarFuncionario(Long empresaid, Funcionario funcionario){
        return empresaUseCase.salvarFuncioario(empresaid, funcionario);
    }

    public List<EmpresaResponse> findAllByCategoria(Long categoriaId) {
        return empresaUseCase.findAllByCategoria(categoriaId);
    }

    public List<LocalTime> buscarHorariosDisponiveis(Long idEmpresa, Long idFuncionario, LocalDate data, int duracaoMinutos) {
        return empresaUseCase.buscarHorariosDisponiveis(idEmpresa, idFuncionario, data, duracaoMinutos);
    }

    public AgendamentoResponse reservarHorario(Long idEmpresa, Long idFuncionario, ReservarHorarioRequest request) {
        return empresaUseCase.reservarHorario(idEmpresa, idFuncionario, request);
    }

    public AgendamentoResponse agendarAleatoriamente(Long idEmpresa, AgendamentoDinamicoRequest request) {
        return empresaUseCase.agendarAleatoriamente(idEmpresa, request);
    }

    public List<EmpresaResponse>  findByDestaque() {
        return empresaUseCase.findByDestaque();
    }
}
