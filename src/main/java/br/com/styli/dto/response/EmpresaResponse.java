package br.com.styli.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class EmpresaResponse {
    private Long id;
    private String nome;
    private String telefone;
    private String endereco;
    private String cidade;
    private String uf;
    private Double latitude;
    private Double longitude;
    private Double avaliacaoMedia;
    private Boolean destaque;
    private List<String> imagens;
}
