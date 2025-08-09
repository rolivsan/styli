package br.com.styli.usecase;

import br.com.styli.domain.exception.NotFoundException;
import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.EmpresaServico;
import br.com.styli.domain.model.FuncionarioServicoEmpresa;
import br.com.styli.dto.response.EmpresaServicoComFuncionarios;
import br.com.styli.repository.EmpresaRepository;
import br.com.styli.repository.FuncionarioServicoEmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ListarServicosComFuncionariosUseCase {

    private final EmpresaRepository empresaRepository;
    private final FuncionarioServicoEmpresaRepository funcServRepo;

    public List<EmpresaServicoComFuncionarios> executar(Long empresaId) {
        // 1) Carrega empresa
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada"));

        // 2) Serviços da empresa
        List<EmpresaServico> servicos = empresa.getServicos() == null ? List.of() : empresa.getServicos();
        if (servicos.isEmpty()) return List.of();

        // 3) Busca habilitações (funcionário x serviço) em LOTE
        List<Long> servicoIds = servicos.stream().map(EmpresaServico::getId).toList();
        List<FuncionarioServicoEmpresa> habilitacoes = funcServRepo.findByEmpresaServico_IdIn(servicoIds);

        // 4) Agrupa habilitações por serviço
        Map<Long, List<FuncionarioServicoEmpresa>> porServico = habilitacoes.stream()
                .collect(Collectors.groupingBy(h -> h.getEmpresaServico().getId()));

        // 5) Monta DTO final
        return servicos.stream().map(s -> {
            List<FuncionarioServicoEmpresa> habilitados = porServico.getOrDefault(s.getId(), List.of());

            var funcionariosDTO = habilitados.stream().map(h ->
                    EmpresaServicoComFuncionarios.FuncionarioDoServico.builder()
                            .id(h.getFuncionario().getId())
                            .nomeCompleto(h.getFuncionario().getNomeCompleto())
                            .telefone(h.getFuncionario().getTelefone())
                            .duracaoMinutosOverride(h.getDuracaoMinutosOverride())
                            .precoOverride(h.getPrecoOverride() != null ? h.getPrecoOverride().toPlainString() : null)
                            .ativo(h.getAtivo())
                            .build()
            ).toList();

            return EmpresaServicoComFuncionarios.builder()
                    .id(s.getId())
                    .nome(s.getNome())
                    .duracaoMinutos(s.getDuracaoMinutos())
                    .preco(s.getPreco().toPlainString())
                    .categoria(s.getCategoria() != null ? s.getCategoria().getNome() : null)
                    .funcionarios(funcionariosDTO)
                    .build();
        }).toList();
    }
}
