package br.com.styli.controller;

import br.com.styli.dto.request.*;
import br.com.styli.dto.response.EmpresaDetalheResponse;
import br.com.styli.dto.response.EmpresaResponse;
import br.com.styli.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @PostMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public List<EmpresaResponse> buscarEmpresas(@RequestBody @Valid BuscarEmpresasRequest req) {
        return empresaService.buscarEmpresas(req);
    }

    // Detalhes da empresa por ID (qualquer usuário autenticado)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public EmpresaDetalheResponse buscarPorId(@PathVariable Long id) {
        return empresaService.buscarPorId(id);
    }

    // Criar empresa (somente ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public EmpresaResponse criarEmpresa(@RequestBody  @Valid CriarEmpresaRequest req) {
        return empresaService.criarEmpresa(req);
    }

    // Criar serviço para uma empresa (ADMIN)
    @PostMapping("/{id}/servicos")
    @PreAuthorize("hasRole('ADMIN')")
    public EmpresaDetalheResponse.ServicoResumo criarServico(@PathVariable Long id, @RequestBody  @Valid CriarEmpresaServicoRequest req) {
        return empresaService.criarServico(id, req);
    }

    // Vincular funcionário à empresa (ADMIN)
    @PostMapping("/{id}/funcionarios")
    @PreAuthorize("hasRole('ADMIN')")
    public EmpresaDetalheResponse.FuncionarioResumo vincularFuncionario(@PathVariable Long id, @RequestBody VincularFuncionarioRequest req) {
        return empresaService.vincularFuncionario(id, req);
    }

    // Listar serviços da empresa (qualquer autenticado)
    @GetMapping("/{id}/servicos")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public List<EmpresaDetalheResponse.ServicoResumo> listarServicos(@PathVariable Long id) {
        return empresaService.listarServicos(id);
    }
}
