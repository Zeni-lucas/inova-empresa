package com.inova_evento.app.repositories;

import com.inova_evento.app.entities.JuradosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JuradosRepository extends JpaRepository<JuradosEntity, Long> {
    @Query("SELECT j FROM JuradosEntity j JOIN j.eventos e WHERE e.id = :eventoId")
    Optional<List<JuradosEntity>> findByEventoId(@Param("eventoId") Long eventoId);
}
