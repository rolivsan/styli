package br.com.styli.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ComentarioResponse {
    private Long idComentario;
    private String comentarioTexto;
    private Integer comentarioNota;
    private LocalDateTime dataComentario;
}
