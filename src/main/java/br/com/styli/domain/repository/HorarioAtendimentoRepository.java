package br.com.styli.domain.repository;

import br.com.styli.domain.model.Funcionario;
import br.com.styli.domain.model.HorarioAtendimentoFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface HorarioAtendimentoRepository extends JpaRepository<HorarioAtendimentoFuncionario, Long> {

    List<HorarioAtendimentoFuncionario> findByFuncionarioAndDiaSemana(Funcionario funcionario, DayOfWeek diaSemana);

}
