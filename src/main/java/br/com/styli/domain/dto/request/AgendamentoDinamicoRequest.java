package br.com.styli.domain.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AgendamentoDinamicoRequest {
    private Long idCliente;
    private Long idServico;
    private LocalDate data;
    private LocalTime horaDesejada;
}
