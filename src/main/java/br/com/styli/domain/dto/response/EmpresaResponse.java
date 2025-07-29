package br.com.styli.domain.dto.response;

import br.com.styli.domain.model.Categoria;
import lombok.Builder;
import java.util.List;

@Builder
public class EmpresaResponse {
    private String nome;
    private Long idEmpresa;
    private Long idCliente;
    private Long idServico;
    private Boolean destaque;
    private List<FuncionarioResponse> funcionarios;
    private List<ClienteResponse> clientes;
    private List<Categoria> categorias;

    @Builder
    public static  class FuncionarioResponse {
        private Long idFuncionario;
        private String nomeUsuario;
        private String nomeCompleto;
    }

    @Builder
    public static  class ClienteResponse {
        private Long idCliente;
        private String nome;
    }

    @Builder
    public static class CategoriaResponse {
        private long idCategoria;
        private String categoriaNome;
    }

}
