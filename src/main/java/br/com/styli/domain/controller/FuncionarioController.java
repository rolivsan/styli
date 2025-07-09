package br.com.styli.domain.controller;


import br.com.styli.domain.model.Funcionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
