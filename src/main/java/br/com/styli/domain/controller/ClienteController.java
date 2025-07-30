package br.com.styli.domain.controller;

import br.com.styli.domain.dto.response.AgendamentoClienteResponse;
import br.com.styli.domain.dto.response.ClienteResponse;
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
    public ResponseEntity <List<ClienteResponse>> findAll(){
        List<ClienteResponse> clienteList= clienteService.findAll();
        return ResponseEntity.status(200).body(clienteList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> findByID(@PathVariable Long id){
        ClienteResponse clientes = clienteService.findById(id);
        return  ResponseEntity.status(200).body(clientes);
    }

    @PostMapping
    public ResponseEntity <ClienteResponse> save(@RequestBody Cliente cliente){
        ClienteResponse clientes = clienteService.save(cliente);
        return ResponseEntity.status(201).body(clientes);
    }

    @GetMapping("/{id}/agendamentos")
    public ResponseEntity<List<AgendamentoClienteResponse>> findAllHorarios(@PathVariable Long id){
        List<AgendamentoClienteResponse> horarioList = clienteService.findAllHorarios(id);
        return ResponseEntity.status(200).body(horarioList);
    }

}
