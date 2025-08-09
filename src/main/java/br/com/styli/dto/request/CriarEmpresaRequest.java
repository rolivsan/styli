package br.com.styli.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CriarEmpresaRequest {
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private String cidade;
    private String uf;
    private Double latitude;
    private Double longitude;
    private String horarioFuncionamento;
    private Boolean destaque;
    private List<String> imagens;
    private List<Long> categoriasIds; // categorias já existentes
}
