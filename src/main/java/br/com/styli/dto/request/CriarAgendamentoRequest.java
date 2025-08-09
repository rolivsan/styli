package br.com.styli.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CriarAgendamentoRequest {
    private Long servicoId;        // << só o serviço (EmpresaServico)
    private Long clienteId;
    private Long funcionarioId;    // opcional (se não vier, escolhe aleatório habilitado)
    private LocalDateTime inicio;  // data/hora desejada
}
