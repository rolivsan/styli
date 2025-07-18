package br.com.styli.domain.controller;


import br.com.styli.domain.model.Empresa;
import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("styli/empresa/funcioario")
public class FuncionarioController {

    @Autowired
    FuncionarioService funcionarioService;

    @GetMapping
    public List<Funcionario> findAll(){
        List<Funcionario> funcionarioList = funcionarioService.findAll();
        return funcionarioList;
    }

    @PostMapping ("/create")
    public ResponseEntity<Funcionario> save(@RequestBody Funcionario funcionario){
        Funcionario funcionario1 = funcionarioService.save(funcionario);
        return ResponseEntity.status(201).body(funcionario);
    }
}
