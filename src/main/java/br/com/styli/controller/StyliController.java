package br.com.styli.controller;

import br.com.styli.dto.request.BuscarEmpresasRequest;
import br.com.styli.dto.request.CriarAgendamentoRequest;
import br.com.styli.dto.response.EmpresaResponse;
import br.com.styli.dto.response.AgendamentoResponse;
import br.com.styli.service.StyliService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StyliController {

    private final StyliService styliService;

    @PostMapping("/empresas/buscar")
    public List<EmpresaResponse> buscarEmpresas(@RequestBody BuscarEmpresasRequest request) {
        return styliService.buscarEmpresas(request);
    }

    @PostMapping("/agendamentos")
    public AgendamentoResponse criarAgendamento(@RequestBody CriarAgendamentoRequest request) {
        return styliService.criarAgendamento(request);
    }
}
