package br.com.styli.domain.controller;


import br.com.styli.domain.model.Comentario;
import br.com.styli.domain.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cliente/comentario")
public class ComentarioController {

    @Autowired
    ComentarioService comentarioService;

    @GetMapping
    public List<Comentario> findAll(){
        List<Comentario> comentarioListList = comentarioService.findAll();
        return comentarioListList;
    }

}
