package br.com.styli.domain.service;

import br.com.styli.domain.model.Categoria;
import br.com.styli.domain.usecase.CategoriaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class CategoriaService {

    @Autowired
    CategoriaUseCase categoriaUseCase;

    public List<Categoria> findAll(){
        List<Categoria> categoriaList = categoriaUseCase.findAll();
        return categoriaList;
    }
}
