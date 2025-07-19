package br.com.styli.domain.usecase;

import br.com.styli.domain.model.Comentario;
import br.com.styli.domain.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ComentarioUseCase {

    @Autowired
    ComentarioRepository comentarioRepository;

    public List<Comentario> findAll(){
        List<Comentario> comentarios = comentarioRepository.findAll();
        return comentarios ;
    }
}
