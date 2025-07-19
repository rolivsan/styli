package br.com.styli.domain.usecase;

import br.com.styli.domain.exception.BusinessException;
import br.com.styli.domain.exception.ErrorCode;
import br.com.styli.domain.model.Cliente;
import br.com.styli.domain.repository.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ClienteUseCase {

    @Autowired
    ClienteRepository clienteRepository;

    public List<Cliente> findAll(){
        List<Cliente> clienteList = clienteRepository.findAll();
        return clienteList;
    }

    public Cliente findById(Long id){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> {
                    log.error("Cliente com ID {} não encontrado", id);
                    return new BusinessException(ErrorCode.CLIENTE_NOT_FOUND);
                });
        return cliente;
    }

    public Cliente save(Cliente clientes){
        Cliente cliente = clienteRepository.save(clientes);
        return cliente;
    }


}
