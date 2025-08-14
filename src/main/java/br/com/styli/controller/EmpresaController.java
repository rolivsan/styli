package br.com.styli.controller;

import br.com.styli.dto.request.*;
import br.com.styli.dto.response.EmpresaDetalheResponse;
import br.com.styli.dto.response.EmpresaResponse;
import br.com.styli.service.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Buscar empresas", description = "Busca empresas filtrando por categoria e localização.")
    @PostMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public List<EmpresaResponse> buscarEmpresas(@RequestBody @Valid BuscarEmpresasRequest req) {
        return empresaService.buscarEmpresas(req);
    }

    @Operation(summary = "Detalhar empresa", description = "Retorna informações detalhadas de uma empresa pelo seu ID.")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public EmpresaDetalheResponse buscarPorId(@PathVariable Long id) {
        return empresaService.buscarPorId(id);
    }

    @Operation(summary = "Criar empresa", description = "Cadastra uma nova empresa (somente ADMIN).")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public EmpresaResponse criarEmpresa(@RequestBody  @Valid CriarEmpresaRequest req) {
        return empresaService.criarEmpresa(req);
    }

    @Operation(summary = "Criar serviço", description = "Adiciona um novo serviço dentro de uma empresa (somente ADMIN).")
    @PostMapping("/{id}/servicos")
    @PreAuthorize("hasRole('ADMIN')")
    public EmpresaDetalheResponse.ServicoResumo criarServico(@PathVariable Long id, @RequestBody  @Valid CriarEmpresaServicoRequest req) {
        return empresaService.criarServico(id, req);
    }

    @Operation(summary = "Vincular funcionário", description = "Vincula um funcionário a uma empresa (somente ADMIN).")
    @PostMapping("/{id}/funcionarios")
    @PreAuthorize("hasRole('ADMIN')")
    public EmpresaDetalheResponse.FuncionarioResumo vincularFuncionario(@PathVariable Long id, @RequestBody VincularFuncionarioRequest req) {
        return empresaService.vincularFuncionario(id, req);
    }

    @Operation(summary = "Listar serviços", description = "Lista todos os serviços de uma empresa.")
    @GetMapping("/{id}/servicos")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public List<EmpresaDetalheResponse.ServicoResumo> listarServicos(@PathVariable Long id) {
        return empresaService.listarServicos(id);
    }
}
