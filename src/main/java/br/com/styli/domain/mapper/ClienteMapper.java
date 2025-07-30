package br.com.styli.domain.mapper;

import br.com.styli.domain.dto.response.AgendamentoClienteResponse;
import br.com.styli.domain.dto.response.ClienteResponse;
import br.com.styli.domain.model.Agendamento;
import br.com.styli.domain.model.Cliente;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.model.Servico;

import java.util.ArrayList;
import java.util.List;

public class ClienteMapper {

    public static ClienteResponse toResponse (Cliente cliente){
        List<ClienteResponse.AgendamentoResponse> agendamentoList = new ArrayList<>();

        List<Agendamento> agendamentos = cliente.getAgendamentos();

        List<ClienteResponse.AgendamentoResponse> agendamentoListOut =  new ArrayList<>();


        for(Agendamento agenda : agendamentos){
            ClienteResponse.AgendamentoResponse agendamentoSaida = ClienteResponse.AgendamentoResponse.builder()
                    .horario(agenda.getHorario())
                    .idServico(new Servico().getId())
                    .idFuncionario(new Funcionario().getId())
                    .horario(agenda.getHorario())
                    .build();
            agendamentoListOut.add(agendamentoSaida);
        }

        return ClienteResponse.builder()
                .idCliente(cliente.getId()!= null ? cliente.getId() : null )
                .name(cliente.getNome())
                .agendamentos(agendamentoListOut)
                .build();
    }

    public static AgendamentoClienteResponse toAgendamentoResponse(Agendamento agnd) {
        AgendamentoClienteResponse.ServicoResponse servicoResponse = null;
        if (agnd.getServico() != null) {
            servicoResponse = AgendamentoClienteResponse.ServicoResponse.builder()
                    .id(agnd.getServico().getId())
                    .nome(agnd.getServico().getNome())
                    .duracaoMinutos(agnd.getServico().getDuracaoMinutos())
                    .preco(agnd.getServico().getPreco())
                    .build();
        }

        AgendamentoClienteResponse.FuncionarioResponse funcionarioResponse = null;
        if (agnd.getFuncionario() != null) {
            funcionarioResponse = AgendamentoClienteResponse.FuncionarioResponse.builder()
                    .id(agnd.getFuncionario().getId())
                    .nomeCompleto(agnd.getFuncionario().getNomeCompleto())
                    .imagens(agnd.getFuncionario().getImagens())
                    .build();
        }

        return AgendamentoClienteResponse.builder()
                .idAgendamento(agnd.getId())
                .horario(agnd.getHorario())
                .servico(servicoResponse)
                .funcionario(funcionarioResponse)
                .build();
    }

}
