package br.com.styli.domain.controller;

import br.com.styli.domain.model.Cliente;
import br.com.styli.domain.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @GetMapping
    public ResponseEntity <List<Cliente>> findAll(){
        List<Cliente> clienteList= clienteService.findAll();
        return ResponseEntity.status(200).body(clienteList);
    }

    @GetMapping("{/id}")
    public ResponseEntity<Cliente> findByID(@PathVariable Long id){
        Cliente clientes = clienteService.findById(id);
        return  ResponseEntity.status(200).body(clientes);
    }

    @PostMapping("{/create}")
    public ResponseEntity <Cliente> save(@RequestBody Cliente cliente){
        Cliente clientes = clienteService.save(cliente);
        return ResponseEntity.status(201).body(clientes);
    }

    
}
