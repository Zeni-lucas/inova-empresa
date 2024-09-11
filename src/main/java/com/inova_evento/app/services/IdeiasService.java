package com.inova_evento.app.services;

import com.inova_evento.app.entities.IdeiasEntity;
import com.inova_evento.app.repositories.IdeiasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IdeiasService {

    @Autowired
    private IdeiasRepository ideiasRepository;

    @Transactional
    public IdeiasEntity save(IdeiasEntity entity){
        return this.ideiasRepository.save(entity);
    }

    @Transactional
    public IdeiasEntity update(IdeiasEntity entity,Long id){
        entity.setId(id);
        return this.ideiasRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public IdeiasEntity findById(Long id){
        IdeiasEntity ideias = this.ideiasRepository.findById(id).get();
        return ideias;
    }

    @Transactional(readOnly = true)
    public List<IdeiasEntity> findAll(){
        List<IdeiasEntity> list = this.ideiasRepository.findAll();
        return list;
    }
}
