package br.com.styli.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmpresaServicoComFuncionarios {
    private Long id;                 // id do EmpresaServico
    private String nome;             // ex.: Corte Masculino
    private Integer duracaoMinutos;  // padrão do serviço na empresa
    private String preco;            // string pra facilitar exibição
    private String categoria;        // nome da categoria (se houver)

    private List<FuncionarioDoServico> funcionarios;

    @Data @Builder
    public static class FuncionarioDoServico {
        private Long id;                // id do funcionário
        private String nomeCompleto;
        private String telefone;

        // overrides (se definidos para este funcionário neste serviço)
        private Integer duracaoMinutosOverride;
        private String precoOverride;   // string (ex.: "55.00")
        private Boolean ativo;          // habilitação ativa
    }
}
