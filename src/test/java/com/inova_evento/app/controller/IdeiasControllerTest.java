package com.inova_evento.app.controller;

import com.inova_evento.app.controllers.IdeiasController;
import com.inova_evento.app.entities.IdeiasEntity;
import com.inova_evento.app.services.IdeiasService;
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
public class IdeiasControllerTest {

    @Autowired
    private IdeiasController ideiasController;

    @MockBean
    private IdeiasService ideiasService;

    private IdeiasEntity ideia;

    @BeforeEach
    void setup() {
        ideia = new IdeiasEntity();
        ideia.setId(1L);
        ideia.setDescricao("Ideia Teste");

        Mockito.when(ideiasService.save(any(IdeiasEntity.class))).thenReturn(ideia);
        Mockito.when(ideiasService.findById(1L)).thenReturn(ideia);
        Mockito.when(ideiasService.findAll()).thenReturn(Arrays.asList(ideia));
    }

    @Test
    @DisplayName("Teste Save Ideia")
    void cenari01() {
        IdeiasEntity novaIdeia = new IdeiasEntity();
        novaIdeia.setDescricao("Nova Ideia");

        ResponseEntity<IdeiasEntity> resposta = ideiasController.save(novaIdeia);

        Assertions.assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        Assertions.assertEquals("Ideia Teste", Objects.requireNonNull(resposta.getBody()).getDescricao());
    }

    @Test
    @DisplayName("Teste Find By Id Ideia")
    void cenario02() {
        ResponseEntity<IdeiasEntity> resposta = ideiasController.findById(1L);

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assertions.assertEquals("Ideia Teste", Objects.requireNonNull(resposta.getBody()).getDescricao());
    }

    @Test
    @DisplayName("Teste Find All Ideias")
    void cenario03() {
        ResponseEntity<List<IdeiasEntity>> resposta = ideiasController.findAll();

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assertions.assertEquals(1, resposta.getBody().size());
    }
}
