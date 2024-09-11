package com.inova_evento.app.repositories;

import com.inova_evento.app.entities.IdeiasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdeiasRepository extends JpaRepository<IdeiasEntity, Long> {
}
