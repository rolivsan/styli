package br.com.styli.controller;

import br.com.styli.dto.request.CriarClienteRequest;
import br.com.styli.dto.response.ClienteResponse;
import br.com.styli.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Criar cliente", description = "Cadastro inicial de cliente.")
    @PostMapping
    public ClienteResponse criar(@RequestBody @Valid CriarClienteRequest req) {
        return clienteService.criar(req);
    }

    @Operation(summary = "Buscar cliente por ID", description = "Retorna dados de um cliente pelo seu ID.")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public ClienteResponse buscar(@PathVariable Long id) {
        return clienteService.buscar(id);
    }
}
