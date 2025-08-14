package br.com.styli.usecase;

import br.com.styli.domain.model.Agendamento;
import br.com.styli.domain.model.StatusAgendamento;
import br.com.styli.dto.request.FiltroAgendamentosRequest;
import br.com.styli.dto.response.AgendamentoResponse;
import br.com.styli.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ListarAgendamentosUseCase {

    private final AgendamentoRepository repo;

    public List<AgendamentoResponse> executar(FiltroAgendamentosRequest f) {
        // interpreta datas (opcional)
        LocalDateTime de = null, ate = null;
        if (f.getDe() != null && !f.getDe().isBlank()) {
            de = LocalDate.parse(f.getDe()).atStartOfDay();
        }
        if (f.getAte() != null && !f.getAte().isBlank()) {
            ate = LocalDate.parse(f.getAte()).atTime(23, 59, 59);
        }

        List<Agendamento> lista;

        if (f.getClienteId() != null) {
            lista = (de != null && ate != null)
                    ? repo.findByCliente_IdAndInicioBetween(f.getClienteId(), de, ate)
                    : repo.findByCliente_Id(f.getClienteId());
        } else if (f.getFuncionarioId() != null) {
            lista = (de != null && ate != null)
                    ? repo.findByFuncionario_IdAndInicioBetween(f.getFuncionarioId(), de, ate)
                    : repo.findByFuncionario_Id(f.getFuncionarioId());
        } else if (f.getEmpresaId() != null) {
            lista = (de != null && ate != null)
                    ? repo.findByEmpresa_IdAndInicioBetween(f.getEmpresaId(), de, ate)
                    : repo.findByEmpresa_Id(f.getEmpresaId());
        } else {
            // se nenhum filtro de entidade vier, devolve vazio para evitar dump geral
            lista = List.of();
        }

        // filtra por status em memória (simples)
        if (f.getStatus() != null && !f.getStatus().isBlank()) {
            StatusAgendamento st;
            try {
                st = StatusAgendamento.valueOf(f.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                // status inválido -> lista vazia
                return List.of();
            }
            lista = lista.stream().filter(a -> a.getStatus() == st).toList();
        }

        return lista.stream().map(a -> AgendamentoResponse.builder()
                .id(a.getId())
                .empresa(a.getEmpresa().getNome())
                .servico(a.getServico().getNome())
                .cliente(a.getCliente().getNome())
                .funcionario(a.getFuncionario() != null ? a.getFuncionario().getNomeCompleto() : null)
                .inicio(a.getInicio())
                .fim(a.getFim())
                .status(a.getStatus().name())
                .build()
        ).toList();
    }
}
