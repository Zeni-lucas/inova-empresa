package com.inova_evento.app.services;

import com.inova_evento.app.entities.EventosEntity;
import com.inova_evento.app.entities.UsuariosEntity;
import com.inova_evento.app.entities.enums.Roles;
import com.inova_evento.app.repositories.EventosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EventosServiceTest {

    @MockBean
    private EventosRepository eventosRepository;

    @MockBean
    private UsuariosService usuariosService;

    @Autowired
    private EventosService eventosService;

    private EventosEntity evento;
    private UsuariosEntity admin;
    private UsuariosEntity colaborador;

    @BeforeEach
    void setup() {
        admin = new UsuariosEntity(1L, "ADMIN", "mamonha@gmail.com", "admin123", Roles.ADMIN,null,null);
        colaborador = new UsuariosEntity(2L, "COLABORADOR", "vitao@gmail.com", "senha123", Roles.COLABORADOR,null,null);

        evento = new EventosEntity(
                1L,
                "Evento Teste",
                "Descrição do Evento Teste",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(3),
                admin,
                null
        );

        Mockito.when(usuariosService.findById(1L)).thenReturn(admin);
        Mockito.when(usuariosService.findById(2L)).thenReturn(colaborador);
        Mockito.when(eventosRepository.save(any(EventosEntity.class))).thenReturn(evento);
        Mockito.when(eventosRepository.findById(1L)).thenReturn(Optional.of(evento));
        Mockito.when(eventosRepository.findAll()).thenReturn(Arrays.asList(
                evento,
                new EventosEntity(
                        2L, "Outro Evento", "Descrição de Outro Evento",
                        LocalDate.now(), LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2), LocalDate.now().plusDays(3),
                        colaborador, null
                )
        ));
    }

    @Test
    @DisplayName("Teste save evento com usuário admin")
    void cenario01() {
        Long userId = 1L;

        Mockito.when(usuariosService.findById(userId)).thenReturn(admin);

        EventosEntity result = eventosService.save(evento, userId);

        assertEquals(evento, result);
        verify(usuariosService).findById(userId);
        verify(eventosRepository).save(evento);
    }

    @Test
    @DisplayName("Teste save evento com usuário colaborador")
    void cenario02() {
        Long userId = 2L;

        Mockito.when(usuariosService.findById(userId)).thenReturn(colaborador);

        assertThrows(SecurityException.class, () -> eventosService.save(evento, userId));
        verify(usuariosService).findById(userId);
        verify(eventosRepository, never()).save(any(EventosEntity.class));
    }

    @Test
    @DisplayName("Teste update evento com sucesso")
    void cenario03() {
        evento.setNome("Evento Atualizado");

        Mockito.when(eventosRepository.save(evento)).thenReturn(evento);

        EventosEntity updatedEvent = eventosService.update(evento, 1L);

        assertEquals(1L, updatedEvent.getId());
        assertEquals("Evento Atualizado", updatedEvent.getNome());
    }

    @Test
    @DisplayName("Teste update evento com falha")
    void cenario04() {
        EventosEntity eventoInexistente = new EventosEntity();
        eventoInexistente.setNome("Evento Inexistente");

        Mockito.when(eventosRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> eventosService.update(eventoInexistente, 3L));
    }

    @Test
    @DisplayName("Teste findById com sucesso")
    void cenario05() {
        EventosEntity foundEvent = eventosService.findById(1L);

        assertEquals(1L, foundEvent.getId());
        assertEquals("Evento Teste", foundEvent.getNome());
    }

    @Test
    @DisplayName("Teste findById com erro")
    void cenario06() {
        Mockito.when(eventosRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> eventosService.findById(2L));
    }

    @Test
    @DisplayName("Teste findAll com sucesso")
    void cenario07() {
        List<EventosEntity> eventos = eventosService.findALl();

        assertEquals(2, eventos.size());
        assertEquals("Evento Teste", eventos.get(0).getNome());
        assertEquals("Outro Evento", eventos.get(1).getNome());
    }

    @Test
    @DisplayName("Teste findAll com lista vazia")
    void cenario08() {
        Mockito.when(eventosRepository.findAll()).thenReturn(List.of());

        List<EventosEntity> eventos = eventosService.findALl();

        assertTrue(eventos.isEmpty());
    }
}
