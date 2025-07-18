package br.com.styli.domain.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class FuncionarioRequest {
    private String nomeUsuario;
    private String nomeCompleto;
    private String senha;
    private String telefone;
    private List<String> imagens;
}
