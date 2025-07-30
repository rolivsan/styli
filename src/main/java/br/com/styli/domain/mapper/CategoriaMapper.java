package br.com.styli.domain.mapper;

import br.com.styli.domain.dto.response.CategoriaResponse;
import br.com.styli.domain.model.Categoria;

public class CategoriaMapper {

    public static CategoriaResponse toResponse (Categoria categoria){
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .build();
    }
}
