package com.inova_evento.app.services;

import com.inova_evento.app.entities.VotosPopularesEntity;
import com.inova_evento.app.exception.BusinnesException;
import com.inova_evento.app.repositories.VotosPopularesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class VotosPopularesService {


    @Autowired
    VotosPopularesRepository votosPopularesRepository;

    @Autowired
    EventosService eventosRepository;

    @Autowired
    IdeiasService ideiasRepository;

    @Autowired
    UsuariosService usuariosRepository;

    @Transactional
    public String votar(Long usuarioId, Long ideiaId) {

        var ideia = ideiasRepository.findById(ideiaId);

        var evento = ideia.getEvento();
        var hoje = LocalDate.now();

        if (hoje.isBefore(evento.getDataInicio()) || hoje.isAfter(evento.getDataAvaliacaoPopular())) {
            throw new BusinnesException("Votação não permitida fora do período de votação.");
        }


        var usuario = usuariosRepository.findById(usuarioId);


        boolean jaVotou = votosPopularesRepository.findAll().stream()
                .anyMatch(voto -> voto.getUsuario().getId().equals(usuarioId) &&
                        voto.getIdeia().getId().equals(ideiaId));
        if (jaVotou) {
            throw new BusinnesException("O colaborador já votou nesta ideia.");
        }


        var voto = new VotosPopularesEntity();
        voto.setUsuario(usuario);
        voto.setIdeia(ideia);
        votosPopularesRepository.save(voto);

        return "Voto registrado com sucesso.";
    }
}
