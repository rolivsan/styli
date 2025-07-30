package br.com.styli.domain.service;

import br.com.styli.domain.dto.response.AgendamentoClienteResponse;
import br.com.styli.domain.dto.response.ClienteResponse;
import br.com.styli.domain.model.Cliente;
import br.com.styli.domain.usecase.ClienteUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    ClienteUseCase clienteUseCase;

    public List<ClienteResponse> findAll(){
        List<ClienteResponse> clienteList = clienteUseCase.findAll();
        return clienteList;
    }

    public ClienteResponse findById(Long id){
        ClienteResponse clientes = clienteUseCase.findById(id);
        return clientes;
    }

    public ClienteResponse save(Cliente clientes){
        ClienteResponse clientes1 = clienteUseCase.save(clientes);
        return clientes1;
    }

    public List<AgendamentoClienteResponse> findAllHorarios(Long id) {
        return clienteUseCase.findAllHorarios(id);
    }
}
