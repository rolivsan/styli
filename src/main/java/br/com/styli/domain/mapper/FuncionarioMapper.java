package br.com.styli.domain.mapper;

import br.com.styli.domain.dto.response.FuncionarioResponse;
import br.com.styli.domain.model.Funcionario;

public class FuncionarioMapper {
    public static FuncionarioResponse toResponse(Funcionario funcionario){
        return FuncionarioResponse.builder()
                .nomeCompleto(funcionario.getNomeCompleto())
                .idFuncionario(funcionario.getId())
                .nomeUsuario(funcionario.getNomeUsuario())
                .build();
    }
}
