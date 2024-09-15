package com.inova_evento.app.controller;

import com.inova_evento.app.controllers.AdminController;
import com.inova_evento.app.entities.EventosEntity;
import com.inova_evento.app.entities.IdeiasEntity;
import com.inova_evento.app.entities.UsuariosEntity;
import com.inova_evento.app.services.AvaliacoesService;
import com.inova_evento.app.services.EventosService;
import com.inova_evento.app.services.JuradosService;
import com.inova_evento.app.services.UsuariosService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
public class AdminControllerTest {

    @Autowired
    private AdminController adminController;

    @MockBean
    private EventosService eventosService;

    @MockBean
    private UsuariosService usuariosService;

    @MockBean
    private JuradosService juradosService;

    @MockBean
    private AvaliacoesService avaliacoesService;

    private EventosEntity evento;
    private UsuariosEntity usuario;
    private IdeiasEntity ideia;

    @BeforeEach
    void setup() {
        evento = new EventosEntity();
        evento.setId(1L);
        evento.setNome("Evento Teste");

        usuario = new UsuariosEntity();
        usuario.setId(1L);
        usuario.setNome("Usuario Teste");

        ideia = new IdeiasEntity();
        ideia.setId(1L);
        ideia.setDescricao("Ideia Teste");

        EventosEntity eventoAtualizado = new EventosEntity();
        eventoAtualizado.setId(1L);
        eventoAtualizado.setNome("Evento Atualizado");

        Mockito.when(eventosService.update(any(EventosEntity.class), eq(1L))).thenReturn(eventoAtualizado);
        Mockito.when(eventosService.findById(1L)).thenReturn(evento);
        Mockito.when(eventosService.save(any(EventosEntity.class))).thenReturn(evento);
        Mockito.when(eventosService.findALl()).thenReturn(Arrays.asList(evento));

        Mockito.when(usuariosService.setRole(eq(1L), any(UsuariosEntity.class))).thenReturn(usuario);
        Mockito.doNothing().when(juradosService).distribuirIdeias(1L);

        Mockito.when(avaliacoesService.findTop10Ideias()).thenReturn(Arrays.asList(ideia));
    }

    @Test
    @DisplayName("Teste Create Evento")
    void cenario01() {
        EventosEntity novoEvento = new EventosEntity();
        novoEvento.setNome("Novo Evento");

        ResponseEntity<EventosEntity> resposta = adminController.create(novoEvento);

        Assertions.assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        Assertions.assertEquals("Evento Teste", Objects.requireNonNull(resposta.getBody()).getNome());
    }

    @Test
    @DisplayName("Teste Update Evento")
    void cenario02() {
        EventosEntity eventoAtualizado = new EventosEntity();
        eventoAtualizado.setId(1L);
        eventoAtualizado.setNome("Evento Atualizado");

        ResponseEntity<EventosEntity> resposta = adminController.update(eventoAtualizado, 1L);

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assertions.assertEquals("Evento Atualizado", Objects.requireNonNull(resposta.getBody()).getNome());
    }

    @Test
    @DisplayName("Teste Find By Id Evento")
    void cenario03() {
        ResponseEntity<EventosEntity> resposta = adminController.findById(1L);

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assertions.assertEquals("Evento Teste", Objects.requireNonNull(resposta.getBody()).getNome());
    }

    @Test
    @DisplayName("Teste Find All Eventos")
    void cenario04() {
        ResponseEntity<List<EventosEntity>> resposta = adminController.findAll();

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assertions.assertEquals(1, resposta.getBody().size());
    }

    @Test
    @DisplayName("Teste Update Role Usuario")
    void cenario05() {
        usuario.setNome("Usuario Atualizado");
        ResponseEntity<UsuariosEntity> resposta = adminController.updateRole(1L, usuario);

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assertions.assertEquals("Usuario Atualizado", Objects.requireNonNull(resposta.getBody()).getNome());
    }

    @Test
    @DisplayName("Teste Distribuir Ideias")
    void cenario06() {
        ResponseEntity<Void> resposta = adminController.distribuirIdeias(1L);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
    }

    @Test
    @DisplayName("Teste Find Top 10 Ideias")
    void cenario07() {
        ResponseEntity<List<IdeiasEntity>> resposta = adminController.findTop10();

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assertions.assertEquals(1, resposta.getBody().size());
    }
}
