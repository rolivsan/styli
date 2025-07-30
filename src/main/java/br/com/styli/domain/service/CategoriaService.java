package br.com.styli.domain.service;

import br.com.styli.domain.dto.response.CategoriaResponse;
import br.com.styli.domain.usecase.CategoriaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    CategoriaUseCase categoriaUseCase;

    public List<CategoriaResponse> findAll(){
        List<CategoriaResponse> categorias = categoriaUseCase.findAll();
        return categorias;
    }
}
