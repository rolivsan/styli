package br.com.styli.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VincularFuncionarioRequest {
    @NotNull(message = "funcionarioId é obrigatório")
    private Long funcionarioId;

    @NotBlank(message = "papel é obrigatório")
    @Size(max = 40, message = "papel deve ter no máximo 40 caracteres")
    private String papel;

    private Boolean ativo = true;
}
