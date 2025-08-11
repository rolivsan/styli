package br.com.styli.usecase;

import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.EmpresaServico;
import br.com.styli.dto.response.EmpresaDetalheResponse;
import br.com.styli.domain.exception.NotFoundException;
import br.com.styli.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BuscarEmpresaPorIdUseCase {

    private final EmpresaRepository empresaRepository;

    public EmpresaDetalheResponse executar(Long id) {
        Empresa e = empresaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada"));

        return EmpresaDetalheResponse.builder()
                .id(e.getId())
                .nome(e.getNome())
                .telefone(e.getTelefone())
                .email(e.getEmail())
                .endereco(e.getEndereco())
                .cidade(e.getCidade())
                .uf(e.getUf())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .horarioFuncionamento(e.getHorarioFuncionamento())
                .destaque(e.getDestaque())
                .imagens(e.getImagens())
                .servicos(e.getServicos() == null ? java.util.List.of() :
                        e.getServicos().stream().map(this::toServicoResumo).collect(Collectors.toList()))
                // funcionários serão preenchidos no service (ListarFuncionariosDaEmpresaUseCase)
                .build();
    }

    private EmpresaDetalheResponse.ServicoResumo toServicoResumo(EmpresaServico s) {
        return EmpresaDetalheResponse.ServicoResumo.builder()
                .id(s.getId())
                .nome(s.getNome())
                .duracaoMinutos(s.getDuracaoMinutos())
                .preco(s.getPreco().toPlainString())
                .categoria(s.getCategoria() != null ? s.getCategoria().getNome() : null)
                .build();
    }
}
