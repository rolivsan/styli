package br.com.styli.domain.controller;

import br.com.styli.domain.dto.response.CategoriaResponse;
import br.com.styli.domain.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> findAll(){
        List<CategoriaResponse> categoriaList= categoriaService.findAll();
        return ResponseEntity.status(200).body(categoriaList);
    }
}
