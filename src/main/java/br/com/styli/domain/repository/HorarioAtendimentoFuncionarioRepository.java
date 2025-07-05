package br.com.styli.domain.repository;

import br.com.styli.domain.model.HorarioAtendimentoFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioAtendimentoFuncionarioRepository extends JpaRepository<HorarioAtendimentoFuncionario, Long> {

}
