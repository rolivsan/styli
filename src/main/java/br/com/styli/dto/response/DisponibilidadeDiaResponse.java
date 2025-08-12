package br.com.styli.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class DisponibilidadeDiaResponse {
    private Long empresaId;
    private Long servicoId;
    private LocalDate data;
    private List<DisponibilidadeFuncionarioResponse> funcionarios;
}
