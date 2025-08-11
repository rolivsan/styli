// AgendamentoRepository.java
package br.com.styli.repository;

import br.com.styli.domain.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByFuncionario_Id(Long funcionarioId);

    // busca simples de conflito (mesmo funcionário com sobreposição)
    boolean existsByFuncionario_IdAndInicioLessThanAndFimGreaterThan(Long funcionarioId, LocalDateTime fim, LocalDateTime inicio);
}
