package br.com.styli.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmpresaDetalheResponse {

    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private String cidade;
    private String uf;
    private Double latitude;
    private Double longitude;
    private String horarioFuncionamento;
    private Boolean destaque;
    private List<String> imagens;

    private List<ServicoResumo> servicos;
    private List<FuncionarioResumo> funcionarios;

    // NOVO CAMPO
    private List<EmpresaServicoComFuncionarios> servicosComFuncionarios;

    @Data
    @Builder
    public static class ServicoResumo {
        private Long id;
        private String nome;
        private Integer duracaoMinutos;
        private String preco;
        private String categoria;
    }

    @Data
    @Builder
    public static class FuncionarioResumo {
        private Long id;
        private String nomeCompleto;
        private String telefone;
    }
}
