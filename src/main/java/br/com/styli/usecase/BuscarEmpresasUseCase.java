package br.com.styli.usecase;

import br.com.styli.dto.request.BuscarEmpresasRequest;
import br.com.styli.dto.response.EmpresaResponse;
import br.com.styli.domain.model.Empresa;
import br.com.styli.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BuscarEmpresasUseCase {

    private final EmpresaRepository empresaRepository;

    public List<EmpresaResponse> executar(BuscarEmpresasRequest request) {
        List<Empresa> empresas = empresaRepository.findByCategorias_Id(request.getCategoriaId());

        return empresas.stream()
                .filter(e -> calcularDistancia(request.getLatitude(), request.getLongitude(),
                        e.getLatitude(), e.getLongitude()) <= request.getRaioKm())
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private EmpresaResponse toResponse(Empresa e) {
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

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }
}
