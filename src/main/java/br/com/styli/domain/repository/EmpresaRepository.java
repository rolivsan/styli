package br.com.styli.domain.repository;

import br.com.styli.domain.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    List<Empresa> findByCategorias_Id(Long categoriaId);

    Optional<Empresa> findByDestaque(Boolean destaque);
}
