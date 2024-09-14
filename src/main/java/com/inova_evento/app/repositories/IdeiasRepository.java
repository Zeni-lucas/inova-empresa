package com.inova_evento.app.repositories;

import com.inova_evento.app.entities.IdeiasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IdeiasRepository extends JpaRepository<IdeiasEntity, Long> {

    Optional<List<IdeiasEntity>> findByEventoId(Long id);
}
