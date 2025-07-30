package br.com.styli.domain.usecase;

import br.com.styli.domain.dto.response.CategoriaResponse;
import br.com.styli.domain.mapper.CategoriaMapper;
import br.com.styli.domain.model.Categoria;
import br.com.styli.domain.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoriaUseCase {

    @Autowired
    CategoriaRepository categoriaRepository;

    public List<CategoriaResponse> findAll(){
        List<CategoriaResponse> categorias  = new ArrayList<>();

        for (Categoria cat : categoriaRepository.findAll()){
            categorias.add(CategoriaMapper.toResponse(cat));
        }
        return categorias;
    }

}
