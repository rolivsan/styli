package br.com.styli.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AgendamentoResponse {
    private Long id;
    private String empresa;
    private String servico;
    private String cliente;
    private String funcionario;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private String status;
}
