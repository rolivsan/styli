package br.com.styli.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CriarAgendamentoRequest {

    @NotNull(message = "servicoId é obrigatório")
    private Long servicoId;

    @NotNull(message = "clienteId é obrigatório")
    private Long clienteId;

    // opcional: quando cliente escolhe pro
    private Long funcionarioId;

    @NotNull(message = "início é obrigatório")
    @Future(message = "início deve ser no futuro")
    private LocalDateTime inicio;
}
