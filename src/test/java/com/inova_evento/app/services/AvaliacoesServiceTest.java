package com.inova_evento.app.services;

import com.inova_evento.app.entities.AvaliacoesEntity;
import com.inova_evento.app.entities.EventosEntity;
import com.inova_evento.app.entities.IdeiasEntity;
import com.inova_evento.app.entities.JuradosEntity;
import com.inova_evento.app.exception.AccessDeniedException;
import com.inova_evento.app.exception.BusinnesException;
import com.inova_evento.app.exception.EntityNotFoundException;
import com.inova_evento.app.repositories.AvaliacoesRepository;
import com.inova_evento.app.repositories.JuradosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AvaliacoesServiceTest {

    @MockBean
    private AvaliacoesRepository avaliacoesRepository;

    @MockBean
    private JuradosRepository juradosRepository;

    @Autowired
    private AvaliacoesService avaliacoesService;

    private AvaliacoesEntity avaliacao;
    private JuradosEntity jurado;
    private IdeiasEntity ideia;
    private EventosEntity evento;

    @BeforeEach
    void setup() {

        jurado = new JuradosEntity();
        jurado.setId(1L);


        evento = new EventosEntity();
        evento.setDataInicio(LocalDate.now().minusDays(1));
        evento.setDataAvaliacaoJurados(LocalDate.now().plusDays(5));

        ideia = new IdeiasEntity();
        ideia.setId(1L);
        ideia.setEvento(evento);

        avaliacao = new AvaliacoesEntity();
        avaliacao.setId(1L);
        avaliacao.setNota(5);
        avaliacao.setJurado(jurado);
        avaliacao.setIdeia(ideia);


        when(avaliacoesRepository.findById(1L)).thenReturn(Optional.of(avaliacao));
        when(juradosRepository.findById(1L)).thenReturn(Optional.of(jurado));
        when(avaliacoesRepository.save(any(AvaliacoesEntity.class))).thenReturn(avaliacao);
        when(avaliacoesRepository.findByIdeiaId(1L)).thenReturn(Optional.of(List.of(avaliacao, avaliacao)));
    }

    @Test
    @DisplayName("Encontrar avaliação por ID com sucesso")
    void cenario01() {
        AvaliacoesEntity resultado = avaliacoesService.findById(1L);

        assertEquals(1L, resultado.getId());

    }

    @Test
    @DisplayName("Erro ao encontrar avaliação por ID")
    void cenario02() {
        when(avaliacoesRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> avaliacoesService.findById(2L));
    }

    @Test
    @DisplayName("Avaliar com sucesso")
    void cenario03() {
        AvaliacoesEntity resultado = avaliacoesService.avaliar(1L, 1L, 8);

        assertEquals(8, resultado.getNota());

    }

    @Test
    @DisplayName("Erro ao avaliar - jurado não autorizado")
    void cenario04() {
        JuradosEntity outroJurado = new JuradosEntity();
        outroJurado.setId(2L);

        when(juradosRepository.findById(2L)).thenReturn(Optional.of(outroJurado));

        assertThrows(AccessDeniedException.class, () -> avaliacoesService.avaliar(2L, 1L, 8));

    }

    @Test
    @DisplayName("Erro ao avaliar - nota fora do intervalo")
    void cenario05() {
        assertThrows(BusinnesException.class, () -> avaliacoesService.avaliar(1L, 1L, 2));
        assertThrows(BusinnesException.class, () -> avaliacoesService.avaliar(1L, 1L, 11));


    }

    @Test
    @DisplayName("Erro ao avaliar - fora do período de avaliação")
    void cenario06() {

        evento.setDataAvaliacaoJurados(LocalDate.of(2023, 9, 5));
        LocalDate hoje = LocalDate.of(2023, 9, 6);
        when(avaliacoesRepository.findById(1L)).thenReturn(Optional.of(avaliacao));

        assertThrows(BusinnesException.class, () -> avaliacoesService.avaliar(1L, 1L, 8));


    }

    @Test
    @DisplayName("Calcular média de notas com sucesso")
    void cenario07() {
        Double media = avaliacoesService.calcularMediaNotas(1L);

        assertEquals(5.0, media);
        verify(avaliacoesRepository, times(1)).findByIdeiaId(1L);
    }

    @Test
    @DisplayName("Erro ao calcular média de notas - menos de dois jurados")
    void cenario08() {
        when(avaliacoesRepository.findByIdeiaId(1L)).thenReturn(Optional.of(List.of(avaliacao)));

        assertThrows(BusinnesException.class, () -> avaliacoesService.calcularMediaNotas(1L));
    }

    @Test
    @DisplayName("Encontrar top 10 ideias com sucesso")
    void cenario09() {
        List<IdeiasEntity> ideias = List.of(ideia);
        when(avaliacoesRepository.findTop10Ideias()).thenReturn(ideias);

        List<IdeiasEntity> resultado = avaliacoesService.findTop10Ideias();

        assertEquals(1, resultado.size());

    }
}
