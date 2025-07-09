package br.com.styli.domain.controller;

import br.com.styli.domain.model.Agendamento;
import br.com.styli.domain.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("styli/cliente/agenda")
public class AgendamentoController {

    @Autowired
    AgendamentoService agendamentoService;

    @GetMapping
    public ResponseEntity<List<Agendamento>> findAll(){
        List<Agendamento> agendamentoList= agendamentoService.findAll();
        return ResponseEntity.status(200).body(agendamentoList);
    }

}
