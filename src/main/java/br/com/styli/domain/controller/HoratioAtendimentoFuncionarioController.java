package br.com.styli.domain.controller;

import br.com.styli.domain.model.HorarioAtendimentoFuncionario;
import br.com.styli.domain.service.HorarioAtendimentoFuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cliente/horario")
public class HoratioAtendimentoFuncionarioController {

    @Autowired
    HorarioAtendimentoFuncionarioService horatioAtendimentoFuncionarioService;

    @GetMapping
    public ResponseEntity<List<HorarioAtendimentoFuncionario>> findAll(){
        List<HorarioAtendimentoFuncionario> horarioList= horatioAtendimentoFuncionarioService.findAll();
        return ResponseEntity.status(200).body(horarioList);
    }
}
