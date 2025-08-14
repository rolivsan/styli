package br.com.styli.dto.request;

import lombok.Data;

@Data
public class FiltroAgendamentosRequest {
    private Long clienteId;      // opcional
    private Long funcionarioId;  // opcional
    private Long empresaId;      // opcional
    private String de;           // opcional - "YYYY-MM-DD"
    private String ate;          // opcional - "YYYY-MM-DD"
    private String status;       // opcional - ex: "RESERVADO" ou "CANCELADO"
}
