package br.com.styli.repository;

import br.com.styli.domain.model.Funcionario;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select f from Funcionario f where f.id = :id")
    Optional<Funcionario> lockById(@Param("id") Long id);
}
