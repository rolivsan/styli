package br.com.styli.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DisponibilidadeFuncionarioResponse {
    private Long funcionarioId;
    private String nomeCompleto;
    private Integer duracaoMinutos;    // duração aplicada para este funcionário (override ou do serviço)
    private List<String> horarios;     // ex: ["09:00","09:15","09:30",...]
}
