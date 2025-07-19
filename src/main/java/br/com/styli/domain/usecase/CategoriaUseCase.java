package br.com.styli.domain.usecase;

import br.com.styli.domain.model.Categoria;
import br.com.styli.domain.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoriaUseCase {

    @Autowired
    CategoriaRepository categoriaRepository;

    public List<Categoria> findAll(){
        List<Categoria> categoriaList = categoriaRepository.findAll();
        return categoriaList;
    }
}
