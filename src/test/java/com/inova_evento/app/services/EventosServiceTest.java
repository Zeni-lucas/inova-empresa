package com.inova_evento.app.services;

import com.inova_evento.app.entities.EventosEntity;
import com.inova_evento.app.entities.UsuariosEntity;
import com.inova_evento.app.entities.enums.Roles;
import com.inova_evento.app.repositories.EventosRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.management.relation.Role;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.inova_evento.app.entities.enums.Roles.ADMIN;
import static com.inova_evento.app.entities.enums.Roles.COLABORADOR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class EventosServiceTest {

    @InjectMocks
    EventosService eventosService;

    @Mock
    EventosRepository eventosRepository;

    @Mock
    UsuariosService usuariosService;

    EventosEntity evento;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        UsuariosEntity admin = new UsuariosEntity(1L, "ADMIN", "mamonha@gmail.com", "admin123", Roles.ADMIN);
        UsuariosEntity colaborador = new UsuariosEntity(2L, "COLABORADOR", "vitao@gmail.com", "senha123", Roles.COLABORADOR);

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

        when(usuariosService.findById(1L)).thenReturn(admin);
        when(usuariosService.findById(2L)).thenReturn(colaborador);
        when(eventosRepository.save(Mockito.any(EventosEntity.class)))
                .thenReturn(evento);
        when(eventosRepository.findById(1L))
                .thenReturn(Optional.of(evento));
        when(eventosRepository.findAll())
                .thenReturn(List.of(evento, new EventosEntity(
                        2L, "Outro Evento", "Descrição de Outro Evento",
                        LocalDate.now(), LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2), LocalDate.now().plusDays(3),
                        colaborador, null
                )));
    }

    @Test
    @DisplayName("TesteSave usuario admin")
    public void testSaveWithAdminUser() {
        Long userId = 1L;
        UsuariosEntity adminUser = new UsuariosEntity();
        adminUser.setRole(Roles.ADMIN);

        EventosEntity evento = new EventosEntity();

        when(usuariosService.findById(userId)).thenReturn(adminUser);
        when(eventosRepository.save(evento)).thenReturn(evento);

        EventosEntity result = eventosService.save(evento, userId);

        verify(usuariosService).findById(userId);
        verify(eventosRepository).save(evento);
        assertEquals(evento, result);
    }

    @Test
    @DisplayName("TestUpdate com sucesso")
    void testUpdateSuccess() {
        evento.setNome("Evento Atualizado");

        when(eventosRepository.save(evento)).thenReturn(evento);

        EventosEntity updatedEvent = eventosService.update(evento, 1L);
        assertEquals(1L, updatedEvent.getId());
        assertEquals("Evento Atualizado", updatedEvent.getNome());
    }

    @Test
    @DisplayName("TestUpdate com falha")
    void testUpdateFailure() {
        EventosEntity eventoInexistente = new EventosEntity();
        eventoInexistente.setNome("Evento Inexistente");

        when(eventosRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            eventosService.update(eventoInexistente, 3L);
        });
    }

    @Test
    @DisplayName("TestFindById sucesso")
    void testFindByIdSuccess() {
        EventosEntity foundEvent = eventosService.findById(1L);
        assertEquals(1L, foundEvent.getId());
        assertEquals("Evento Teste", foundEvent.getNome());
    }

    @Test
    @DisplayName("TestFindById com erro")
    void testFindByIdFailure() {
        when(eventosRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            eventosService.findById(2L);
        });
    }

    // Testes para o método findAll
    @Test
    @DisplayName("TestFindAll com sucesso")
    void testFindAllWithEvents() {
        List<EventosEntity> eventos = eventosService.findALl();
        assertEquals(2, eventos.size());
        assertEquals("Evento Teste", eventos.get(0).getNome());
        assertEquals("Outro Evento", eventos.get(1).getNome());
    }

    @Test
    @DisplayName("TestFindAll com erro")
    void testFindAllWithoutEvents() {
        when(eventosRepository.findAll()).thenReturn(List.of());

        List<EventosEntity> eventos = eventosService.findALl();
        assertTrue(eventos.isEmpty());
    }
}
