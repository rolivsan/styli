package br.com.styli.domain.repository;

import br.com.styli.domain.model.Agendamento;
import br.com.styli.domain.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByFuncionarioAndData(Funcionario funcionario, LocalDate data);

}
