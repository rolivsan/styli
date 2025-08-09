package br.com.styli.dto.request;

import lombok.Data;

@Data
public class BuscarEmpresasRequest {
    private Long categoriaId;
    private Double latitude;
    private Double longitude;
    private Double raioKm;
}
