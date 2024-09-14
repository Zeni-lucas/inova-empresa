package com.inova_evento.app.services;

import com.inova_evento.app.entities.AvaliacoesEntity;
import com.inova_evento.app.entities.IdeiasEntity;
import com.inova_evento.app.entities.JuradosEntity;
import com.inova_evento.app.entities.enums.Roles;
import com.inova_evento.app.exception.AccessDeniedException;
import com.inova_evento.app.exception.BusinnesException;
import com.inova_evento.app.exception.EntityNotFoundException;
import com.inova_evento.app.repositories.AvaliacoesRepository;
import com.inova_evento.app.repositories.IdeiasRepository;
import com.inova_evento.app.repositories.JuradosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class JuradosService {

    @Autowired
    JuradosRepository juradosRepository;

    @Autowired
    UsuariosService usuariosService;

    @Autowired
    IdeiasService ideiasService;

    @Autowired
    AvaliacoesService avaliacaoService;

    @Autowired
    AvaliacoesRepository avaliacaoRepository;

    @Transactional
    public JuradosEntity save(Long id, JuradosEntity jurado) {

        var usuarios = usuariosService.findById(id);
        if (!usuarios.getRole().equals(Roles.ADMIN)){
            throw new AccessDeniedException("Usuario não autorizado");
        }
        return juradosRepository.save(jurado);
    }

    @Transactional(readOnly = true)
    public JuradosEntity findById(Long id){
        return juradosRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Não Encontrado"));
    }

    @Transactional(readOnly = true)
    public List<JuradosEntity> findAll() {
        return juradosRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<JuradosEntity> findByEventoId(Long id){
        return juradosRepository.findByEventoId(id).orElseThrow(() -> new EntityNotFoundException("Evento não encontrado"));
    }

    //id do evento
    @Transactional
    public void distribuirIdeias(Long id) {
        var ideias = ideiasService.findByEventoId(id);
        var jurados = findByEventoId(id);

        if(jurados.size() < 2){
            throw new BusinnesException("Deve haver no minimo dois jurados para avaliar");
        }

        //eu n fiz essa parte, cerebro pequenininho obrigado DEUS POR INVENTAR O CHATGPT
        Collections.shuffle(ideias);
        for (int i = 0; i < ideias.size(); i++) {
            IdeiasEntity ideia = ideias.get(i);

            JuradosEntity jurado1 = jurados.get(i % jurados.size());
            JuradosEntity jurado2 = jurados.get((i + 1) % jurados.size());

            avaliacaoRepository.save(new AvaliacoesEntity(null, ideia, jurado1, null));
            avaliacaoRepository.save(new AvaliacoesEntity(null, ideia, jurado2, null));
        }
    }

}
