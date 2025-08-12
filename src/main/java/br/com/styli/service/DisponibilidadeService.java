package br.com.styli.service;

import br.com.styli.dto.response.DisponibilidadeDiaResponse;
import br.com.styli.usecase.ListarDisponibilidadeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DisponibilidadeService {

    private final ListarDisponibilidadeUseCase useCase;

    public DisponibilidadeDiaResponse listar(Long empresaId, Long servicoId, LocalDate data, Long funcionarioId) {
        return useCase.executar(empresaId, servicoId, data, funcionarioId);
    }
}
