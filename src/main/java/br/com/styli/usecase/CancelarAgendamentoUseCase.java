package br.com.styli.usecase;

import br.com.styli.domain.exception.BadRequestException;
import br.com.styli.domain.exception.NotFoundException;
import br.com.styli.domain.model.Agendamento;
import br.com.styli.domain.model.StatusAgendamento;
import br.com.styli.dto.request.CancelarAgendamentoRequest;
import br.com.styli.dto.response.AgendamentoResponse;
import br.com.styli.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CancelarAgendamentoUseCase {

    private final AgendamentoRepository agendamentoRepository;

    public AgendamentoResponse executar(Long id, CancelarAgendamentoRequest req) {
        Agendamento a = agendamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agendamento não encontrado"));

        if (a.getStatus() == StatusAgendamento.CANCELADO) {
            throw new BadRequestException("Agendamento já está cancelado");
        }

        a.setStatus(StatusAgendamento.CANCELADO);
        if (req != null && req.getMotivo() != null && !req.getMotivo().isBlank()) {
            a.setMotivoCancelamento(req.getMotivo().trim());
        }
        a.setCanceladoEm(LocalDateTime.now());

        agendamentoRepository.save(a);

        return AgendamentoResponse.builder()
                .id(a.getId())
                .empresa(a.getEmpresa().getNome())
                .servico(a.getServico().getNome()) // se seu getter for getServico(), ajuste: a.getServico().getNome()
                .cliente(a.getCliente().getNome())
                .funcionario(a.getFuncionario() != null ? a.getFuncionario().getNomeCompleto() : null)
                .inicio(a.getInicio())
                .fim(a.getFim())
                .status(a.getStatus().name())
                .build();
    }
}
