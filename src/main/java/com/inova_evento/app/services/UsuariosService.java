package com.inova_evento.app.services;

import com.inova_evento.app.entities.UsuariosEntity;
import com.inova_evento.app.repositories.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
public class UsuariosService {
    @Autowired
    private UsuariosRepository usuariosRepository;

    @Transactional
    public UsuariosEntity save(UsuariosEntity usuarios){
        return this.usuariosRepository.save(usuarios);
    }

    @Transactional
    public UsuariosEntity update(UsuariosEntity usuarios, Long id){
        usuarios.setId(id);
        return this.usuariosRepository.save(usuarios);
    }

    @Transactional
    public boolean delete(Long id){
        this.usuariosRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public UsuariosEntity findById(Long id){
        UsuariosEntity user = this.usuariosRepository.findById(id).get();
        return user;
    }

    @Transactional(readOnly = true)
    public List<UsuariosEntity> findALl(){
        List<UsuariosEntity> list = this.usuariosRepository.findAll();
        return list;
    }

}
