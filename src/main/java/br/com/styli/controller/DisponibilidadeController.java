package br.com.styli.controller;

import br.com.styli.dto.response.DisponibilidadeDiaResponse;
import br.com.styli.service.DisponibilidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class DisponibilidadeController {

    private final DisponibilidadeService disponibilidadeService;

    // GET /api/empresas/{id}/disponibilidade?servicoId=...&data=YYYY-MM-DD&funcionarioId=opcional
    @GetMapping("/{empresaId}/disponibilidade")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','FUNCIONARIO')")
    public DisponibilidadeDiaResponse listar(
            @PathVariable Long empresaId,
            @RequestParam Long servicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Long funcionarioId
    ) {
        return disponibilidadeService.listar(empresaId, servicoId, data, funcionarioId);
    }
}
