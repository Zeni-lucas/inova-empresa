package com.inova_evento.app.services;

import com.inova_evento.app.entities.EventosEntity;
import com.inova_evento.app.entities.UsuariosEntity;
import com.inova_evento.app.entities.enums.Roles;
import com.inova_evento.app.repositories.EventosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventosService {

    @Autowired
    private EventosRepository eventosRepository;

    @Autowired
    private UsuariosService usuariosService;


    @Transactional
    public EventosEntity save(EventosEntity evento, Long userId) {
        UsuariosEntity usuario = usuariosService.findById(userId);
        if (usuario == null || usuario.getRole() == null || !usuario.getRole().equals(Roles.ADMIN)) {
            throw new SecurityException("Usuário não autorizado");
        }
        return eventosRepository.save(evento);
    }

    @Transactional
    public EventosEntity update(EventosEntity evento, Long id) {
        if (!eventosRepository.findById(id).isPresent()) {
            throw new RuntimeException("Evento não encontrado");
        }
        return eventosRepository.save(evento);
    }

    @Transactional(readOnly = true)
    public EventosEntity findById(Long id){
        EventosEntity evento = this.eventosRepository.findById(id).get();
        return evento;
    }

    @Transactional(readOnly = true)
    public List<EventosEntity> findALl(){
        List<EventosEntity> list = this.eventosRepository.findAll();
        return list;
    }
}
