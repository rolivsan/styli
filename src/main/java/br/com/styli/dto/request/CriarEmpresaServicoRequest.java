package br.com.styli.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CriarEmpresaServicoRequest {
    @NotBlank(message = "Nome do serviço é obrigatório")
    private String nome;

    @NotNull(message = "Duração é obrigatória")
    @Positive(message = "Duração deve ser positiva (minutos)")
    private Integer duracaoMinutos;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.00", inclusive = false, message = "Preço deve ser maior que zero")
    private BigDecimal preco;

    private Long categoriaId;
    private Boolean ativo = true;
}
