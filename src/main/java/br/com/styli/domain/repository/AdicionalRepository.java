package br.com.styli.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdicionalRepository extends JpaRepository<Adicional, Long> {

    void deleteByOrdemDeServico(OrdemDeServico ordemDeServico);

}
