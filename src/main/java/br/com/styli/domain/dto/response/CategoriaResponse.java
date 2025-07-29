package br.com.styli.domain.dto.response;

import lombok.Builder;

@Builder
public class CategoriaResponse {
    private long idCategoria;
    private String categoriaNome;
}
