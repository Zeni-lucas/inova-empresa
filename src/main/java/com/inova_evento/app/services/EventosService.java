package com.inova_evento.app.services;

import com.inova_evento.app.entities.EventosEntity;
import com.inova_evento.app.repositories.EventosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventosService {

    @Autowired
    private EventosRepository eventosRepository;

    @Transactional
    public EventosEntity save(EventosEntity eventos){
        return this.eventosRepository.save(eventos);
    }

    @Transactional
    public EventosEntity update(EventosEntity entity, Long id){
        entity.setId(id);
        return this.eventosRepository.save(entity);
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
