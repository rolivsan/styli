package br.com.styli.domain.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public class AgendamentoClienteResponse {

    private Long idAgendamento;
    private LocalDateTime horario;
    private ServicoResponse servico;
    private FuncionarioResponse funcionario;

    @Builder
    public static class ServicoResponse{
        private Long id;
        private String nome;
        private Integer duracaoMinutos;
        private BigDecimal preco;
    }

    @Builder
    public static class  FuncionarioResponse{
        private Long id;
        private List<String> imagens;
        private String nomeCompleto;
    }

}
