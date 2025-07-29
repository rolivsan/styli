package br.com.styli.domain.mapper;

import br.com.styli.domain.dto.response.EmpresaResponse;
import br.com.styli.domain.model.Categoria;
import br.com.styli.domain.model.Cliente;
import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.Funcionario;

import java.util.ArrayList;
import java.util.List;

public class EmpresaMapper {
    public static EmpresaResponse toResponse (Empresa empresa){
        List<EmpresaResponse.FuncionarioResponse> funcionariosSaida = new ArrayList<>();
        List<Funcionario> funcionariosSalvos = empresa.getFuncionarios();

        List<EmpresaResponse.ClienteResponse> clienteSaida = new ArrayList<>();
        List<Cliente> clientesSalvos = empresa.getClientes();

        List<EmpresaResponse.CategoriaResponse> categoriaSaida = new ArrayList<>();
        List<Categoria> categoriasSalvas = empresa.getCategorias();

        for (Funcionario fun: funcionariosSalvos){
            EmpresaResponse.FuncionarioResponse funcionarioSaida = EmpresaResponse.FuncionarioResponse.builder()
                    .idFuncionario(fun.getId())
                    .nomeCompleto(fun.getNomeCompleto())
                    .nomeUsuario(fun.getNomeUsuario())
                    .build();
            funcionariosSaida.add(funcionarioSaida);
        }

        for(Cliente cli : clientesSalvos){
            EmpresaResponse.ClienteResponse clientesSaida = EmpresaResponse.ClienteResponse.builder()
                    .idCliente(cli.getId())
                    .nome(cli.getNome())
                    .build();
            clienteSaida.add(clientesSaida);
        }

        for(Categoria cat : categoriasSalvas){
            EmpresaResponse.CategoriaResponse categoriasSaida = EmpresaResponse.CategoriaResponse.builder()
                    .idCategoria(cat.getId())
                    .categoriaNome(cat.getNome())
                    .build();
            categoriaSaida.add(categoriasSaida);
        }

        return EmpresaResponse.builder()
                .idEmpresa(empresa.getId())
                .nome(empresa.getNome())
                .clientes(clienteSaida)
                .destaque(empresa.getDestaque())
                .categorias(categoriasSalvas)
                .funcionarios(funcionariosSaida)
                .build();
    }
}
