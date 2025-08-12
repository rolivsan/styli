package br.com.styli.service;

import br.com.styli.dto.request.CancelarAgendamentoRequest;
import br.com.styli.dto.request.FiltroAgendamentosRequest;
import br.com.styli.dto.response.AgendamentoResponse;
import br.com.styli.usecase.CancelarAgendamentoUseCase;
import br.com.styli.usecase.ListarAgendamentosUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final CancelarAgendamentoUseCase cancelarUseCase;
    private final ListarAgendamentosUseCase listarUseCase;

    public AgendamentoResponse cancelar(Long id, CancelarAgendamentoRequest req) {
        return cancelarUseCase.executar(id, req);
    }

    public List<AgendamentoResponse> listar(FiltroAgendamentosRequest f) {
        return listarUseCase.executar(f);
    }
}
