package com.inova_evento.app.services;

import com.inova_evento.app.entities.JuradosEntity;
import com.inova_evento.app.entities.enums.Roles;
import com.inova_evento.app.exception.AccessDeniedException;
import com.inova_evento.app.exception.EntityNotFoundException;
import com.inova_evento.app.repositories.JuradosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JuradosService {

    @Autowired
    JuradosRepository juradosRepository;

    @Autowired
    UsuariosService usuariosService;

    public JuradosEntity save(Long id, JuradosEntity jurado) {

        var usuarios = usuariosService.findById(id);
        if (!usuarios.getRole().equals(Roles.ADMIN)){
            throw new AccessDeniedException("Usuario não autorizado");
        }
        return juradosRepository.save(jurado);
    }

    public JuradosEntity findById(Long id){
        return juradosRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Não Encontrado"));
    }

    public List<JuradosEntity> findAll() {
        return juradosRepository.findAll();
    }
}
