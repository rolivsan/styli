package br.com.styli.domain.mapper;

import br.com.styli.domain.dto.response.ComentarioResponse;
import br.com.styli.domain.model.Comentario;

public class ComentarioMapper {

    public static ComentarioResponse toResponse (Comentario comentario){
        return ComentarioResponse.builder()
                .idComentario(comentario.getId())
                .comentarioTexto(comentario.getTexto())
                .comentarioNota(comentario.getNota())
                .dataComentario(comentario.getDataComentario())
                .build();
    }


}
