package br.com.styli.usecase;

import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.FuncionarioEmpresa;
import br.com.styli.dto.response.EmpresaDetalheResponse;
import br.com.styli.domain.exception.NotFoundException;
import br.com.styli.repository.EmpresaRepository;
import br.com.styli.repository.FuncionarioEmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListarFuncionariosDaEmpresaUseCase {

    private final EmpresaRepository empresaRepository;
    private final FuncionarioEmpresaRepository funcionarioEmpresaRepository;

    public List<EmpresaDetalheResponse.FuncionarioResumo> executar(Long empresaId) {
        Empresa emp = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada"));

        List<FuncionarioEmpresa> vinculos = funcionarioEmpresaRepository.findAll()
                .stream().filter(v -> v.getEmpresa().getId().equals(emp.getId()) && Boolean.TRUE.equals(v.getAtivo()))
                .toList();

        return vinculos.stream().map(v -> EmpresaDetalheResponse.FuncionarioResumo.builder()
                        .id(v.getFuncionario().getId())
                        .nomeCompleto(v.getFuncionario().getNomeCompleto())
                        .telefone(v.getFuncionario().getTelefone())
                        .build())
                .toList();
    }
}
