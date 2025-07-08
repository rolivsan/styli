package br.com.styli.domain.service;

import br.com.styli.domain.model.Comentario;
import br.com.styli.domain.model.Servico;
import br.com.styli.domain.usecase.ServicoUseCase;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ServicoServices {

    @Autowired
    ServicoUseCase servicoUseCase;

    public List<Servico> findAll(){
        List<Servico> servicoList =servicoUseCase.findAll();
        return servicoList;
    }
}
