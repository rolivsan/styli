package br.com.styli.domain.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public class ClienteResponse {
    private long idCliente;
    private String name;
    private List<AgendamentoResponse> agendamentos;


    @Builder
    public static class AgendamentoResponse {
        private Long idFuncionario;
        private Long idServico;
        private LocalDateTime horario;

        @Builder
        public static class ServicoResponse {
            private Long id;
        }
    }

}



