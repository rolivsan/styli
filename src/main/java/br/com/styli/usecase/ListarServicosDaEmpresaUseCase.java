package br.com.styli.usecase;

import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.EmpresaServico;
import br.com.styli.dto.response.EmpresaDetalheResponse;
import br.com.styli.domain.exception.NotFoundException;
import br.com.styli.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListarServicosDaEmpresaUseCase {

    private final EmpresaRepository empresaRepository;

    public List<EmpresaDetalheResponse.ServicoResumo> executar(Long empresaId) {
        Empresa emp = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada"));

        List<EmpresaServico> servicos = emp.getServicos() == null ? List.of() : emp.getServicos();

        return servicos.stream().map(s -> EmpresaDetalheResponse.ServicoResumo.builder()
                .id(s.getId())
                .nome(s.getNome())
                .duracaoMinutos(s.getDuracaoMinutos())
                .preco(s.getPreco().toPlainString())
                .categoria(s.getCategoria() != null ? s.getCategoria().getNome() : null)
                .build()).toList();
    }
}
