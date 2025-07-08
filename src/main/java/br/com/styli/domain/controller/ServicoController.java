package br.com.styli.domain.controller;

import br.com.styli.domain.model.Comentario;
import br.com.styli.domain.model.Servico;
import br.com.styli.domain.service.ServicoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/cliente/servico")
public class ServicoController {

    @Autowired
    ServicoServices servicoServices;

    public List<Servico> findAll(){
        List<Servico> servicoList = servicoServices.findAll();
        return servicoList;
    }

}
