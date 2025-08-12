package br.com.styli.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CriarClienteRequest {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 80, message = "Nome deve ter entre 2 e 80 caracteres")
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(min = 8, max = 20, message = "Telefone inválido")
    private String telefone;
}
