package br.com.styli.usecase;

import br.com.styli.domain.model.Cliente;
import br.com.styli.dto.request.CriarClienteRequest;
import br.com.styli.dto.response.ClienteResponse;
import br.com.styli.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CriarClienteUseCase {

    private final ClienteRepository clienteRepository;

    public ClienteResponse executar(CriarClienteRequest req) {
        Cliente c = Cliente.builder()
                .nome(req.getNome().trim())
                .telefone(req.getTelefone().trim())
                .build();
        c = clienteRepository.save(c);
        return ClienteResponse.builder()
                .id(c.getId())
                .nome(c.getNome())
                .telefone(c.getTelefone())
                .build();
    }
}
