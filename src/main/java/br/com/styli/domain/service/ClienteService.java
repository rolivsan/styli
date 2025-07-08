package br.com.styli.domain.service;

import br.com.styli.domain.model.Cliente;
import br.com.styli.domain.usecase.ClienteUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    ClienteUseCase clienteUseCase;

    public List<Cliente> findAll(){
        List<Cliente> clienteList = clienteUseCase.findAll();
        return clienteList;
    }

    public Cliente findById(Long id){
        Cliente clientes = clienteUseCase.findById(id);
        return clientes;
    }

    public Cliente save(Cliente clientes){
        Cliente clientes1 = clienteUseCase.save(clientes);
        return clientes1;
    }

}
