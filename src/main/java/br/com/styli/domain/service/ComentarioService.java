package br.com.styli.domain.service;

import br.com.styli.domain.model.Comentario;
import br.com.styli.domain.usecase.ComentarioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    ComentarioUseCase comentarioUseCase;

    public List<Comentario> findAll(){
        List<Comentario> comentarioList = comentarioUseCase.findAll();
        return comentarioList ;
    }
}
