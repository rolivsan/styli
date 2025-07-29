package br.com.styli.domain.dto.response;

import br.com.styli.domain.model.Servico;
import lombok.Builder;

import java.util.List;

@Builder
public class FuncionarioResponse {
    private Long idFuncionario;
    private String nomeUsuario;
    private String nomeCompleto;
    private List<Servico> servicos;


}
