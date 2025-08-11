package br.com.styli.service;

import br.com.styli.dto.request.CriarClienteRequest;
import br.com.styli.dto.response.ClienteResponse;
import br.com.styli.usecase.BuscarClientePorIdUseCase;
import br.com.styli.usecase.CriarClienteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final CriarClienteUseCase criarClienteUseCase;
    private final BuscarClientePorIdUseCase buscarClientePorIdUseCase;

    public ClienteResponse criar(CriarClienteRequest req) {
        return criarClienteUseCase.executar(req);
    }

    public ClienteResponse buscar(Long id) {
        return buscarClientePorIdUseCase.executar(id);
    }
}
