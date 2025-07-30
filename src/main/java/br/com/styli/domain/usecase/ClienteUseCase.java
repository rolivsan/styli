package br.com.styli.domain.usecase;

import br.com.styli.domain.dto.response.AgendamentoClienteResponse;
import br.com.styli.domain.dto.response.ClienteResponse;
import br.com.styli.domain.exception.BusinessException;
import br.com.styli.domain.exception.ErrorCode;
import br.com.styli.domain.mapper.ClienteMapper;
import br.com.styli.domain.model.Agendamento;
import br.com.styli.domain.model.Cliente;
import br.com.styli.domain.repository.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ClienteUseCase {

    @Autowired
    ClienteRepository clienteRepository;

    public List<ClienteResponse> findAll(){
        List<ClienteResponse> clientes = new ArrayList<>();
        for(Cliente cli : clienteRepository.findAll()){
            clientes.add(ClienteMapper.toResponse(cli));
        }
        return clientes;
    }

    public ClienteResponse findById(Long id){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> {
                    log.error("Cliente com ID {} não encontrado", id);
                    return new BusinessException(ErrorCode.CLIENTE_NOT_FOUND);
                });
        return ClienteMapper.toResponse(cliente);
    }

    public ClienteResponse save(Cliente clientes){
        Cliente cliente = clienteRepository.save(clientes);
        return ClienteMapper.toResponse(cliente);
    }

    public List<AgendamentoClienteResponse> findAllHorarios(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(()
                -> new BusinessException(ErrorCode.CLIENTE_NOT_FOUND));

        List<AgendamentoClienteResponse> agendamentos =  new ArrayList<>();

        for(Agendamento agnd : cliente.getAgendamentos()){
            agendamentos.add(ClienteMapper.toAgendamentoResponse(agnd));
        }

        return agendamentos;
    }
}
