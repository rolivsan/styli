package br.com.styli.controller;

import br.com.styli.dto.request.CancelarAgendamentoRequest;
import br.com.styli.dto.response.AgendamentoResponse;
import br.com.styli.service.AgendamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;

    // CANCELAR (qualquer papel)
    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public AgendamentoResponse cancelar(@PathVariable Long id, @RequestBody(required = false) CancelarAgendamentoRequest req) {
        return service.cancelar(id, req);
    }

    // LISTAR
    // Ex: /api/agendamentos?clienteId=1&de=2030-01-01&ate=2030-01-31&status=RESERVADO
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public List<AgendamentoResponse> listar(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long funcionarioId,
            @RequestParam(required = false) Long empresaId,
            @RequestParam(required = false) String de,
            @RequestParam(required = false) String ate,
            @RequestParam(required = false) String status
    ) {
        var f = new br.com.styli.dto.request.FiltroAgendamentosRequest();
        f.setClienteId(clienteId);
        f.setFuncionarioId(funcionarioId);
        f.setEmpresaId(empresaId);
        f.setDe(de);
        f.setAte(ate);
        f.setStatus(status);
        return service.listar(f);
    }
}
