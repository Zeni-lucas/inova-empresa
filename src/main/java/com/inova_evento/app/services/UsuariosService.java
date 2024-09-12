package com.inova_evento.app.services;

import com.inova_evento.app.entities.UsuariosEntity;
import com.inova_evento.app.exception.EntityNotFoundException;
import com.inova_evento.app.exception.UniqueEmailException;
import com.inova_evento.app.repositories.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuariosService {
    @Autowired
    private UsuariosRepository usuariosRepository;

    @Transactional
    public UsuariosEntity save(UsuariosEntity usuarios){
        try {
            return this.usuariosRepository.save(usuarios);
        } catch (org.springframework.dao.DataIntegrityViolationException e){
            throw new UniqueEmailException("Erro ao criar o usuario");
        }

    }

    @Transactional
    public UsuariosEntity update(UsuariosEntity usuario, Long id){

        var user = findById(id);
        atualizarInformacoes(user, usuario);
        return user;
    }

    @Transactional
    public void delete(Long id){
        this.usuariosRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public UsuariosEntity findById(Long id){
        return usuariosRepository.findById(id).orElseThrow( () -> new EntityNotFoundException(String.format("Usuário id=%s não encontrado", id)));
    }

    @Transactional(readOnly = true)
    public List<UsuariosEntity> findALl(){
        return usuariosRepository.findAll();
    }


    private void atualizarInformacoes(UsuariosEntity usuarioExistente, UsuariosEntity novoUsuario) {
        if (novoUsuario.getSenha() != null && !usuarioExistente.getSenha().equals(novoUsuario.getSenha())) {
            usuarioExistente.setSenha(novoUsuario.getSenha());
        }
        if (novoUsuario.getEmail() != null && !usuarioExistente.getEmail().equals(novoUsuario.getEmail())) {
            if (usuariosRepository.existsByEmail(novoUsuario.getEmail())) {
                throw new UniqueEmailException(String.format("O email %s já está em uso.", novoUsuario.getEmail()));
            }
        }
        if (novoUsuario.getNome() != null && !usuarioExistente.getNome().equals(novoUsuario.getNome())) {
            usuarioExistente.setNome(novoUsuario.getNome());
        }
    }

}
