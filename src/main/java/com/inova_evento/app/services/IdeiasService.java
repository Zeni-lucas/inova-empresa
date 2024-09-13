package com.inova_evento.app.services;

import com.inova_evento.app.entities.IdeiasEntity;
import com.inova_evento.app.entities.UsuariosEntity;
import com.inova_evento.app.entities.enums.Roles;
import com.inova_evento.app.exception.BusinnesException;
import com.inova_evento.app.exception.EntityNotFoundException;
import com.inova_evento.app.repositories.IdeiasRepository;
import com.inova_evento.app.repositories.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class IdeiasService {

    @Autowired
    private IdeiasRepository ideiasRepository;

    @Autowired
    private UsuariosService usuariosService;

    @Transactional
    public IdeiasEntity save(IdeiasEntity entity){
        List<UsuariosEntity> usuarios= new ArrayList<>();
        for(UsuariosEntity u: entity.getUsuarios()){
            var userAdd = usuariosService.findById(u.getId());
            usuarios.add(userAdd);
            if (!userAdd.getRole().equals(Roles.COLABORADOR)){
                throw new BusinnesException("O usuario vinculado a ideia deve ser um colaborador");
            }
            if (userAdd.getIdeia() != null){
                throw new BusinnesException("O usuario só pode estar vinculado a uma ideia");
            }
        }
        for (UsuariosEntity u : usuarios) {
            u.setIdeia(entity);
        }
        return this.ideiasRepository.save(entity);
    }


    @Transactional(readOnly = true)
    public IdeiasEntity findById(Long id){

        return ideiasRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ideia não encontrada")
        );
    }

    @Transactional(readOnly = true)
    public List<IdeiasEntity> findAll(){
        List<IdeiasEntity> list = this.ideiasRepository.findAll();
        return list;
    }
}
