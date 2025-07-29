package br.com.styli.domain.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public class EmpresaResponse {

    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private String horarioFuncionamento;
    private String logoUrl;
    private Boolean destaque;
    private List<String> imagens;
    private String instagram;
    private Double avaliacaoMedia;
    private Integer quantidadeAvaliacoes;
    private List<CategoriaResponse> categorias;
    private List<FuncionarioResponse> funcionarios;

    @Builder
    public static class CategoriaResponse{
        private Long id;
        private String nome;
    }

    @Builder
    public static class FuncionarioResponse{
        private Long id;
        private String nomeUsuario;
        private String nomeCompleto;
        private String telefone;
        List<ServicosResponse> servicos;
    }

    @Builder
    public static class ServicosResponse{
        private Long id;
        private String nome;
        private Integer duracaoMinutos;
        private BigDecimal preco;
    }

}
