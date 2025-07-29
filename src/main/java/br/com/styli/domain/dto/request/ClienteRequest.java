package br.com.styli.domain.dto.request;

import br.com.styli.domain.model.Agendamento;

import java.util.List;

public class ClienteRequest {
    private Long id;
    private String nome;
    private String telefone;
    private List<Agendamento> agendamentos;
}
