package br.com.styli.repository;

import br.com.styli.domain.model.FuncionarioServicoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FuncionarioServicoEmpresaRepository extends JpaRepository<FuncionarioServicoEmpresa, Long> {

    List<FuncionarioServicoEmpresa> findByEmpresaServico_IdAndAtivoTrue(Long empresaServicoId);

    Optional<FuncionarioServicoEmpresa> findByFuncionario_IdAndEmpresaServico_IdAndAtivoTrue(
            Long funcionarioId, Long empresaServicoId
    );

    List<FuncionarioServicoEmpresa> findByEmpresaServico_IdIn(Collection<Long> empresaServicoIds);

}
