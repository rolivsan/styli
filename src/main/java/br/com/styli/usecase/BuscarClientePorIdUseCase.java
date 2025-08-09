package br.com.styli.usecase;

import br.com.styli.domain.exception.NotFoundException;
import br.com.styli.domain.model.Cliente;
import br.com.styli.dto.response.ClienteResponse;
import br.com.styli.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BuscarClientePorIdUseCase {

    private final ClienteRepository clienteRepository;

    public ClienteResponse executar(Long id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
        return ClienteResponse.builder()
                .id(c.getId())
                .nome(c.getNome())
                .telefone(c.getTelefone())
                .build();
    }
}
