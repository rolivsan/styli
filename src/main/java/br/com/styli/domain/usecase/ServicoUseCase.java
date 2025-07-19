package br.com.styli.domain.usecase;

import br.com.styli.domain.model.Servico;
import br.com.styli.domain.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServicoUseCase {

    @Autowired
    ServicoRepository servicoRepository;

    public List<Servico> findAll(){
        List<Servico> servicos = servicoRepository.findAll();
        return servicos;
    }
}
