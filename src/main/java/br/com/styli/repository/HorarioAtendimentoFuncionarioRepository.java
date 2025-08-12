package br.com.styli.repository;

import br.com.styli.domain.model.HorarioAtendimentoFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface HorarioAtendimentoFuncionarioRepository extends JpaRepository<HorarioAtendimentoFuncionario, Long> {
    List<HorarioAtendimentoFuncionario> findByFuncionario_IdAndDiaSemanaAndAtivoTrue(Long funcionarioId, DayOfWeek diaSemana);
}
