package br.com.styli.domain.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AgendamentoResponse {
    private Long idAgendamento;
    private Long idFuncionario;
    private Long idCliente;
    private Long idServico;
    private LocalDateTime horario;
}
