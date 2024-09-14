package com.inova_evento.app.repositories;

import com.inova_evento.app.entities.VotosPopularesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotosPopularesRepository extends JpaRepository<VotosPopularesEntity, Long> {
}
