package br.com.styli.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CriarEmpresaServicoRequest {
    private String nome;
    private Integer duracaoMinutos;
    private BigDecimal preco;
    private Long categoriaId; // opcional
    private Boolean ativo = true;
}
