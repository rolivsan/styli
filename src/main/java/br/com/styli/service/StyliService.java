package br.com.styli.service;

import br.com.styli.dto.request.BuscarEmpresasRequest;
import br.com.styli.dto.request.CriarAgendamentoRequest;
import br.com.styli.dto.response.EmpresaResponse;
import br.com.styli.dto.response.AgendamentoResponse;
import br.com.styli.usecase.BuscarEmpresasUseCase;
import br.com.styli.usecase.CriarAgendamentoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StyliService {

    private final BuscarEmpresasUseCase buscarEmpresasUseCase;
    private final CriarAgendamentoUseCase criarAgendamentoUseCase;

    public List<EmpresaResponse> buscarEmpresas(BuscarEmpresasRequest request) {
        return buscarEmpresasUseCase.executar(request);
    }

    public AgendamentoResponse criarAgendamento(CriarAgendamentoRequest request) {
        return criarAgendamentoUseCase.executar(request);
    }
}
