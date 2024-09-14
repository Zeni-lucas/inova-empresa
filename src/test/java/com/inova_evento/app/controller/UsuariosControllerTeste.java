package com.inova_evento.app.controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inova_evento.app.controllers.UsuariosController;
import com.inova_evento.app.entities.UsuariosEntity;
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
public class UsuariosControllerTeste {

    @Autowired
    UsuariosController usuariosController;

    @MockBean
    UsuariosService usuariosService;

    UsuariosEntity mamonha;
    UsuariosEntity vitor;

    @BeforeEach
    void setup(){
        mamonha = new UsuariosEntity(1L,"Mamonha Cardoso", "mamonha@gmail.com", "mamonha123", null,null,null,null);
        vitor = new UsuariosEntity(2L, "Vitor Tdah", "tdah@gmail.com", "tdah123", null,null,null,null);

        Mockito.when(usuariosService.save(any(UsuariosEntity.class))).thenReturn(mamonha);
        Mockito.when(usuariosService.findById(1L)).thenReturn(mamonha);
        Mockito.when(usuariosService.findById(2L)).thenReturn(vitor);
        Mockito.when(usuariosService.findALl()).thenReturn(Arrays.asList(mamonha,vitor));
        Mockito.when(usuariosService.update(any(UsuariosEntity.class), eq(1L))).thenReturn(mamonha);
        Mockito.doNothing().when(usuariosService).delete(1L);
    }

    @Test
    @DisplayName("Teste Save")
    void cenario01(){
        ResponseEntity<UsuariosEntity> retorno = usuariosController.save(mamonha);

        Assertions.assertEquals(HttpStatus.CREATED, retorno.getStatusCode());
        Assertions.assertEquals("Mamonha Cardoso", Objects.requireNonNull(retorno.getBody()).getNome());
    }

    @Test
    @DisplayName("Teste findById")
    void cenario02(){
        ResponseEntity<UsuariosEntity> retorno = usuariosController.findById(1L);

        Assertions.assertEquals(HttpStatus.OK,retorno.getStatusCode());
        Assertions.assertEquals("Mamonha Cardoso", Objects.requireNonNull(retorno.getBody()).getNome());

    }

    @Test
    @DisplayName("Teste findAll")
    void cenario03(){
        ResponseEntity<List<UsuariosEntity>> retorno = usuariosController.findAll();

        Assertions.assertEquals(HttpStatus.OK,retorno.getStatusCode());
        Assertions.assertEquals(2,retorno.getBody().size());
    }

    @Test
    @DisplayName("Teste Update")
    void cenario04(){
        mamonha.setNome("mamonha atualizado");
        ResponseEntity<UsuariosEntity> retorno = usuariosController.update(1L,mamonha);

        Assertions.assertEquals(HttpStatus.OK,retorno.getStatusCode());
        Assertions.assertEquals("mamonha atualizado", Objects.requireNonNull(retorno.getBody()).getNome());
    }

    @Test
    @DisplayName("Teste deletar")
    void cenario05(){
        ResponseEntity<Void> retorno = usuariosController.delete(1L);

        Assertions.assertEquals(HttpStatus.NO_CONTENT,retorno.getStatusCode());

    }

}
