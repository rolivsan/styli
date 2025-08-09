package br.com.styli.usecase;

import br.com.styli.domain.model.Categoria;
import br.com.styli.domain.model.Empresa;
import br.com.styli.dto.request.CriarEmpresaRequest;
import br.com.styli.dto.response.EmpresaResponse;
import br.com.styli.repository.CategoriaRepository;
import br.com.styli.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CriarEmpresaUseCase {

    private final EmpresaRepository empresaRepository;
    private final CategoriaRepository categoriaRepository;

    public EmpresaResponse executar(CriarEmpresaRequest req) {
        List<Categoria> cats = req.getCategoriasIds() == null || req.getCategoriasIds().isEmpty()
                ? List.of()
                : categoriaRepository.findAllById(req.getCategoriasIds());

        Empresa e = Empresa.builder()
                .nome(req.getNome())
                .telefone(req.getTelefone())
                .email(req.getEmail())
                .endereco(req.getEndereco())
                .cidade(req.getCidade())
                .uf(req.getUf())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .horarioFuncionamento(req.getHorarioFuncionamento())
                .destaque(Boolean.TRUE.equals(req.getDestaque()))
                .imagens(req.getImagens())
                .categorias(cats)
                .build();

        e = empresaRepository.save(e);

        return EmpresaResponse.builder()
                .id(e.getId())
                .nome(e.getNome())
                .telefone(e.getTelefone())
                .endereco(e.getEndereco())
                .cidade(e.getCidade())
                .uf(e.getUf())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .avaliacaoMedia(0.0)
                .destaque(e.getDestaque())
                .imagens(e.getImagens())
                .build();
    }
}
