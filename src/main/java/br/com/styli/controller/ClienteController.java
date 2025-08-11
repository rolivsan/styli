package br.com.styli.controller;

import br.com.styli.dto.request.CriarClienteRequest;
import br.com.styli.dto.response.ClienteResponse;
import br.com.styli.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    // Cadastro de cliente (liberado para facilitar onboarding)
    @PostMapping
    public ClienteResponse criar(@RequestBody @Valid CriarClienteRequest req) {
        return clienteService.criar(req);
    }

    // Consultar cliente por id (qualquer autenticado)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public ClienteResponse buscar(@PathVariable Long id) {
        return clienteService.buscar(id);
    }
}
