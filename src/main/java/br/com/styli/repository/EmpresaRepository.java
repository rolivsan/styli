package br.com.styli.repository;

import br.com.styli.domain.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    List<Empresa> findByCategorias_Id(Long categoriaId);
}
