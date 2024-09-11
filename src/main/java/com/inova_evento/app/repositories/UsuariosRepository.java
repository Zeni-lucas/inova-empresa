package com.inova_evento.app.repositories;

import com.inova_evento.app.entities.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosRepository extends JpaRepository<UsuariosEntity,Long> {
}
