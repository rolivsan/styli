package br.com.styli.domain.mapper;

import br.com.styli.domain.dto.response.EmpresaResponse;
import br.com.styli.domain.model.Categoria;
import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.model.Servico;

import java.util.List;

public class EmpresaMapper {

    public static EmpresaResponse toResponse(Empresa empresa) {
        // Categorias
        List<EmpresaResponse.CategoriaResponse> categoriaResponses = new java.util.ArrayList<>();
        if (empresa.getCategorias() != null) {
            for (Categoria categoria : empresa.getCategorias()) {
                EmpresaResponse.CategoriaResponse categoriaResponse =
                        EmpresaResponse.CategoriaResponse.builder()
                                .id(categoria.getId())
                                .nome(categoria.getNome())
                                .build();
                categoriaResponses.add(categoriaResponse);
            }
        }

        // Funcionários
        List<EmpresaResponse.FuncionarioResponse> funcionarioResponses = new java.util.ArrayList<>();
        if (empresa.getFuncionarios() != null) {
            for (Funcionario funcionario : empresa.getFuncionarios()) {
                // Serviços
                List<EmpresaResponse.ServicosResponse> servicoResponses = new java.util.ArrayList<>();
                if (funcionario.getServicos() != null) {
                    for (Servico servico : funcionario.getServicos()) {
                        EmpresaResponse.ServicosResponse servicoResponse =
                                EmpresaResponse.ServicosResponse.builder()
                                        .id(servico.getId())
                                        .nome(servico.getNome())
                                        .duracaoMinutos(servico.getDuracaoMinutos())
                                        .preco(servico.getPreco())
                                        .build();
                        servicoResponses.add(servicoResponse);
                    }
                }

                EmpresaResponse.FuncionarioResponse funcionarioResponse =
                        EmpresaResponse.FuncionarioResponse.builder()
                                .id(funcionario.getId())
                                .nomeUsuario(funcionario.getNomeUsuario())
                                .nomeCompleto(funcionario.getNomeCompleto())
                                .telefone(funcionario.getTelefone())
                                .servicos(servicoResponses)
                                .build();
                funcionarioResponses.add(funcionarioResponse);
            }
        }

        // Empresa
        return EmpresaResponse.builder()
                .id(empresa.getId())
                .nome(empresa.getNome())
                .telefone(empresa.getTelefone())
                .email(empresa.getEmail())
                .endereco(empresa.getEndereco())
                .horarioFuncionamento(empresa.getHorarioFuncionamento())
                .logoUrl(empresa.getLogoUrl())
                .destaque(empresa.getDestaque())
                .imagens(empresa.getImagens())
                .instagram(empresa.getInstagram())
                .avaliacaoMedia(empresa.getAvaliacaoMedia())
                .quantidadeAvaliacoes(empresa.getQuantidadeAvaliacoes())
                .categorias(categoriaResponses)
                .funcionarios(funcionarioResponses)
                .build();
    }

}
