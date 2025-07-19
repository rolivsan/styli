package br.com.styli.domain.repository;

import br.com.styli.domain.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByIdAndEmpresaId(Long id, Long empresaId);

    List<Funcionario> findByEmpresaIdAndServicos_Id(Long empresaId, Long servicoId);

}
