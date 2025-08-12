package br.com.styli.repository;

import br.com.styli.domain.model.BloqueioAgenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BloqueioAgendaRepository extends JpaRepository<BloqueioAgenda, Long> {

    // qualquer bloqueio que sobreponha o intervalo [inicioDia, fimDia]
    List<BloqueioAgenda> findByFuncionario_IdAndInicioLessThanAndFimGreaterThan(Long funcionarioId, LocalDateTime fim, LocalDateTime inicio);
}
