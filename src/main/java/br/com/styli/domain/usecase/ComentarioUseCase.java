package br.com.styli.domain.usecase;

import br.com.styli.domain.model.Comentario;
import br.com.styli.domain.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ComentarioUseCase {

    @Autowired
    ComentarioRepository comentarioRepository;

    public List<Comentario> findAll(){
        List<Comentario> comentarioList = comentarioRepository.findAll();
        return comentarioList ;
    }
}
