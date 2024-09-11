package com.inova_evento.app.repositories;

import com.inova_evento.app.entities.EventosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventosRepository extends JpaRepository<EventosEntity, Long> {
}
