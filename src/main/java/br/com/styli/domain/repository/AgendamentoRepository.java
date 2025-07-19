package br.com.styli.domain.repository;

import br.com.styli.domain.model.Agendamento;
import br.com.styli.domain.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("SELECT a FROM Agendamento a WHERE a.funcionario = :funcionario AND DATE(a.horario) = :data")
    List<Agendamento> findByFuncionarioAndData(@Param("funcionario") Funcionario funcionario,
                                               @Param("data") LocalDate data);

}
