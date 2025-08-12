package br.com.styli.repository;

import br.com.styli.domain.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByCliente_Id(Long clienteId);
    List<Agendamento> findByFuncionario_Id(Long funcionarioId);
    List<Agendamento> findByEmpresa_Id(Long empresaId);

    List<Agendamento> findByCliente_IdAndInicioBetween(Long clienteId, LocalDateTime de, LocalDateTime ate);
    List<Agendamento> findByFuncionario_IdAndInicioBetween(Long funcionarioId, LocalDateTime de, LocalDateTime ate);
    List<Agendamento> findByEmpresa_IdAndInicioBetween(Long empresaId, LocalDateTime de, LocalDateTime ate);

    boolean existsByFuncionario_IdAndInicioLessThanAndFimGreaterThan(Long funcionarioId, LocalDateTime fim, LocalDateTime inicio);
}
