package com.inova_evento.app.repositories;

import com.inova_evento.app.entities.AvaliacoesEntity;
import com.inova_evento.app.entities.IdeiasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface AvaliacoesRepository extends JpaRepository<AvaliacoesEntity, Long> {
    Optional<List<AvaliacoesEntity>> findByIdeiaId(Long id);
    @Query("SELECT i FROM IdeiasEntity i " +
            "JOIN AvaliacoesEntity a ON i.id = a.ideia.id " +
            "GROUP BY i.id " +
            "ORDER BY AVG(a.nota) DESC")
    List<IdeiasEntity> findTop10Ideias();
}
