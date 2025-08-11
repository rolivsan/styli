package br.com.styli.usecase;

import br.com.styli.domain.model.Categoria;
import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.EmpresaServico;
import br.com.styli.dto.request.CriarEmpresaServicoRequest;
import br.com.styli.dto.response.EmpresaDetalheResponse;
import br.com.styli.domain.exception.NotFoundException;
import br.com.styli.repository.CategoriaRepository;
import br.com.styli.repository.EmpresaRepository;
import br.com.styli.repository.EmpresaServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CriarEmpresaServicoUseCase {

    private final EmpresaRepository empresaRepository;
    private final CategoriaRepository categoriaRepository;
    private final EmpresaServicoRepository servicoRepository;

    public EmpresaDetalheResponse.ServicoResumo executar(Long empresaId, CriarEmpresaServicoRequest req) {
        Empresa emp = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new NotFoundException("Empresa não encontrada"));

        Categoria cat = null;
        if (req.getCategoriaId() != null) {
            cat = categoriaRepository.findById(req.getCategoriaId())
                    .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
        }

        EmpresaServico s = EmpresaServico.builder()
                .empresa(emp)
                .nome(req.getNome())
                .duracaoMinutos(req.getDuracaoMinutos())
                .preco(req.getPreco())
                .ativo(req.getAtivo())
                .categoria(cat)
                .build();

        s = servicoRepository.save(s);

        return EmpresaDetalheResponse.ServicoResumo.builder()
                .id(s.getId())
                .nome(s.getNome())
                .duracaoMinutos(s.getDuracaoMinutos())
                .preco(s.getPreco().toPlainString())
                .categoria(cat != null ? cat.getNome() : null)
                .build();
    }
}
