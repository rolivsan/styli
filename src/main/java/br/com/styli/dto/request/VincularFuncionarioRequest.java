package br.com.styli.dto.request;

import lombok.Data;

@Data
public class VincularFuncionarioRequest {
    private Long funcionarioId;
    private String papel; // Barbeiro, Gerente, etc.
    private Boolean ativo = true;
}
