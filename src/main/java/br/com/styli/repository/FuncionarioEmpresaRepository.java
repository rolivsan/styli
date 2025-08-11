package br.com.styli.repository;

import br.com.styli.domain.model.FuncionarioEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncionarioEmpresaRepository extends JpaRepository<FuncionarioEmpresa, Long> {
    Optional<FuncionarioEmpresa> findByFuncionario_IdAndEmpresa_IdAndAtivoTrue(Long funcionarioId, Long empresaId);
}
