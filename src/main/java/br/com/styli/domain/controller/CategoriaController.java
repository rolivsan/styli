package br.com.styli.domain.controller;

import br.com.styli.domain.model.Categoria;
import br.com.styli.domain.model.Cliente;
import br.com.styli.domain.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("styli/categoria")
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> findAll(){
        List<Categoria> categoriaList= categoriaService.findAll();
        return ResponseEntity.status(200).body(categoriaList);
    }
}
