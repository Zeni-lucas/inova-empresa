package com.inova_evento.app.services;

import com.inova_evento.app.entities.AvaliacoesEntity;
import com.inova_evento.app.entities.EventosEntity;
import com.inova_evento.app.entities.IdeiasEntity;
import com.inova_evento.app.exception.AccessDeniedException;
import com.inova_evento.app.exception.BusinnesException;
import com.inova_evento.app.exception.EntityNotFoundException;
import com.inova_evento.app.repositories.AvaliacoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvaliacoesService {

    @Autowired
    AvaliacoesRepository avaliacoesRepository;

    @Autowired
    JuradosService juradosService;

    @Transactional(readOnly = true)
    public AvaliacoesEntity findById(Long id) {
        return avaliacoesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ideia não encontrada"));
    }

    @Transactional
    public AvaliacoesEntity avaliar(Long juradoId, Long avaliacaoId, Integer nota){

        var avaliacao = findById(avaliacaoId);
        var jurado = juradosService.findById(juradoId);
        if (!avaliacao.getJurado().getId().equals(juradoId)) {
            throw new AccessDeniedException("Jurado não autorizado a avaliar esta ideia");
        }
        if (nota < 3 || nota > 10) {
            throw new BusinnesException("Nota deve estar entre 3 e 10");
        }

        LocalDate hoje = LocalDate.now();
        EventosEntity evento = avaliacao.getIdeia().getEvento();
        if (hoje.isBefore(evento.getDataInicio()) || hoje.isAfter(evento.getDataAvaliacaoJurados())) {
            throw new BusinnesException("Avaliação não permitida fora do período de avaliação.");
        }

        avaliacao.setNota(nota);
        return avaliacoesRepository.save(avaliacao);
    }

    @Transactional(readOnly = true)
    public Double calcularMediaNotas(Long ideiaId) {
        var avaliacoes = avaliacoesRepository.findByIdeiaId(ideiaId).orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada"));

        if (avaliacoes.size() != 2) {
            throw new BusinnesException("A ideia deve ser avaliada por dois jurados");
        }

        return avaliacoes.stream()
                .mapToDouble(AvaliacoesEntity::getNota)
                .average()
                .orElseThrow(() -> new BusinnesException("Erro ao calcular a média das notas"));
    }

    @Transactional(readOnly = true)
    public List<IdeiasEntity> findTop10Ideias() {
        return avaliacoesRepository.findTop10Ideias();
    }
}
