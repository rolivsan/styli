package br.com.styli.usecase;

import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.model.FuncionarioEmpresa;
import br.com.styli.dto.request.VincularFuncionarioRequest;
import br.com.styli.dto.response.EmpresaDetalheResponse;
import br.com.styli.domain.exception.NotFoundException;
import br.com.styli.repository.EmpresaRepository;
import br.com.styli.repository.FuncionarioEmpresaRepository;
import br.com.styli.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class VincularFuncionarioEmpresaUseCase {

    private final EmpresaRepository empresaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioEmpresaRepository funcionarioEmpresaRepository;

    public EmpresaDetalheResponse.FuncionarioResumo executar(Long empresaId, VincularFuncionarioRequest req) {
        Empresa emp = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada"));

        Funcionario f = funcionarioRepository.findById(req.getFuncionarioId())
                .orElseThrow(() -> new NotFoundException("Funcionário não encontrado"));

        FuncionarioEmpresa fe = FuncionarioEmpresa.builder()
                .empresa(emp)
                .funcionario(f)
                .papel(req.getPapel())
                .ativo(Boolean.TRUE.equals(req.getAtivo()))
                .dataInicio(LocalDate.now())
                .build();

        funcionarioEmpresaRepository.save(fe);

        return EmpresaDetalheResponse.FuncionarioResumo.builder()
                .id(f.getId())
                .nomeCompleto(f.getNomeCompleto())
                .telefone(f.getTelefone())
                .build();
    }
}
