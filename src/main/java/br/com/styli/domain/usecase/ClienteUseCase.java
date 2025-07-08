package br.com.styli.domain.usecase;

import br.com.styli.domain.model.Cliente;
import br.com.styli.domain.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClienteUseCase {

    @Autowired
    ClienteRepository clienteRepository;

    public List<Cliente> findAll(){
        List<Cliente> clienteList = clienteRepository.findAll();
        return clienteList;
    }

    public Cliente findById(Long id){
        Optional<Cliente> clientes = clienteRepository.findById(id);
        return clientes.get();
    }

    public Cliente save(Cliente clientes){
        Cliente clientes1 = clienteRepository.save(clientes);
        return clientes1;
    }


}
