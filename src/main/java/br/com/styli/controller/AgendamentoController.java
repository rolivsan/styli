package br.com.styli.controller;

import br.com.styli.dto.request.CancelarAgendamentoRequest;
import br.com.styli.dto.request.CriarAgendamentoRequest;
import br.com.styli.dto.request.FiltroAgendamentosRequest;
import br.com.styli.dto.response.AgendamentoResponse;
import br.com.styli.security.AuthorizationService;
import br.com.styli.security.JwtService;
import br.com.styli.service.AgendamentoService;
import br.com.styli.usecase.CriarAgendamentoUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;
    private final AuthorizationService authz;
    private final CriarAgendamentoUseCase criarAgendamentoUseCase;
    private final JwtService jwtService;

    // --- CRIAR ---
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public AgendamentoResponse criar(@RequestBody @Valid CriarAgendamentoRequest req, HttpServletRequest http) {
        // Regra opcional: CLIENTE só cria para ele mesmo
        boolean isCliente = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"));
        if (isCliente) {
            String auth = http.getHeader("Authorization");
            if (auth == null || !auth.startsWith("Bearer ")) {
                throw new org.springframework.security.access.AccessDeniedException("Token ausente");
            }
            Long clienteIdToken = jwtService.getClienteId(auth.substring(7))
                    .orElseThrow(() -> new IllegalArgumentException("Token sem clienteId"));
            if (!clienteIdToken.equals(req.getClienteId())) {
                throw new org.springframework.security.access.AccessDeniedException("Cliente só pode criar para si mesmo");
            }
        }
        return criarAgendamentoUseCase.executar(req);
    }

    // --- CANCELAR ---
    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public AgendamentoResponse cancelar(@PathVariable Long id,
                                        @RequestBody(required = false) CancelarAgendamentoRequest req,
                                        HttpServletRequest http) {
        authz.ensurePodeCancelar(id, http);
        return service.cancelar(id, req);
    }

    // --- LISTAR (APENAS ESTE, sem sobrecarga!) ---
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public List<AgendamentoResponse> listar(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long funcionarioId,
            @RequestParam(required = false) Long empresaId,
            @RequestParam(required = false) String de,
            @RequestParam(required = false) String ate,
            @RequestParam(required = false) String status,
            HttpServletRequest http
    ) {
        var f = new FiltroAgendamentosRequest();
        f.setClienteId(clienteId);
        f.setFuncionarioId(funcionarioId);
        f.setEmpresaId(empresaId);
        f.setDe(de);
        f.setAte(ate);
        f.setStatus(status);

        // aplica limitação por papel usando claims do JWT
        authz.applyFilterByRole(f, http);

        return service.listar(f);
    }
}
