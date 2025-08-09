package br.com.styli.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CriarClienteRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;
}
