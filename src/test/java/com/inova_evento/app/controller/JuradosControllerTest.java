package com.inova_evento.app.controller;

import com.inova_evento.app.controllers.JuradosController;
import com.inova_evento.app.entities.AvaliacoesEntity;
import com.inova_evento.app.entities.JuradosEntity;
import com.inova_evento.app.entities.UsuariosEntity;
import com.inova_evento.app.services.AvaliacoesService;
import com.inova_evento.app.services.JuradosService;
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
public class JuradosControllerTest {

    @Autowired
    private JuradosController juradosController;

    @MockBean
    private JuradosService juradosService;

    @MockBean
    private AvaliacoesService avaliacoesService;

    private JuradosEntity jurado;
    private AvaliacoesEntity avaliacao;
    private UsuariosEntity usuario;

    @BeforeEach
    void setup() {
        usuario = new UsuariosEntity();
        usuario.setId(1L);
        usuario.setNome("Usuario Teste");

        jurado = new JuradosEntity();
        jurado.setId(1L);
        jurado.setUsuario(usuario);

        AvaliacoesEntity avaliacao = new AvaliacoesEntity();
        avaliacao.setId(1L);
        avaliacao.setNota(8);
        avaliacao.setJurado(jurado);

        Mockito.when(juradosService.save(eq(1L), any(JuradosEntity.class))).thenReturn(jurado);
        Mockito.when(juradosService.findById(1L)).thenReturn(jurado);
        Mockito.when(juradosService.findAll()).thenReturn(Arrays.asList(jurado));
        Mockito.when(avaliacoesService.avaliar(eq(1L), eq(1L), eq(8))).thenReturn(avaliacao);
    }

    @Test
    @DisplayName("Teste Save")
    void cenari01() {
        JuradosEntity novoJurado = new JuradosEntity();
        novoJurado.setUsuario(new UsuariosEntity());
        ResponseEntity<JuradosEntity> resposta = juradosController.save(1L, novoJurado);

        Assertions.assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        Assertions.assertEquals("Usuario Teste", Objects.requireNonNull(resposta.getBody()).getUsuario().getNome());
    }

    @Test
    @DisplayName("Teste Find By Id")
    void cenario02() {
        ResponseEntity<JuradosEntity> resposta = juradosController.findById(1L);

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assertions.assertEquals("Usuario Teste", Objects.requireNonNull(resposta.getBody()).getUsuario().getNome());
    }

    @Test
    @DisplayName("Teste Find All")
    void cenario03() {
        ResponseEntity<List<JuradosEntity>> resposta = juradosController.findAll();

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assertions.assertEquals(1, resposta.getBody().size());
    }

    @Test
    @DisplayName("Teste Avaliar")
    void cenario04() {
        ResponseEntity<AvaliacoesEntity> resposta = juradosController.avaliar(1L, 1L, 8);

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assertions.assertEquals(8, Objects.requireNonNull(resposta.getBody()).getNota());
    }
}
