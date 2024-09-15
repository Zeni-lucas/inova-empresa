package com.inova_evento.app.services;

import com.inova_evento.app.entities.IdeiasEntity;
import com.inova_evento.app.entities.UsuariosEntity;
import com.inova_evento.app.entities.enums.Roles;
import com.inova_evento.app.exception.BusinnesException;
import com.inova_evento.app.exception.EntityNotFoundException;
import com.inova_evento.app.repositories.IdeiasRepository;
import com.inova_evento.app.repositories.UsuariosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class IdeiasServiceTest {

    @MockBean
    private IdeiasRepository ideiasRepository;

    @MockBean
    private UsuariosRepository usuariosRepository;

    @Autowired
    private IdeiasService ideiasService;

    private IdeiasEntity ideia;
    private UsuariosEntity usuarioColaborador;
    private UsuariosEntity usuarioOutro;

    @BeforeEach
    void setup() {
        ideia = new IdeiasEntity();
        ideia.setId(1L);
        ideia.setUsuarios(new ArrayList<>());

        usuarioColaborador = new UsuariosEntity();
        usuarioColaborador.setId(1L);
        usuarioColaborador.setRole(Roles.COLABORADOR);

        usuarioOutro = new UsuariosEntity();
        usuarioOutro.setId(2L);
        usuarioOutro.setRole(Roles.ADMIN);

        Mockito.when(ideiasRepository.findById(1L)).thenReturn(Optional.of(ideia));
        Mockito.when(ideiasRepository.findAll()).thenReturn(List.of(ideia));
        Mockito.when(ideiasRepository.save(any(IdeiasEntity.class))).thenReturn(ideia);

        Mockito.when(usuariosRepository.findById(1L)).thenReturn(Optional.of(usuarioColaborador));
        Mockito.when(usuariosRepository.findById(2L)).thenReturn(Optional.of(usuarioOutro));
    }

    @Test
    @DisplayName("Salvar ideia com sucesso")
    void salvarIdeiaComSucesso() {
        ideia.getUsuarios().add(usuarioColaborador);

        IdeiasEntity resultado = ideiasService.save(ideia);

        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Salvar ideia com erro de função de usuário")
    void salvarIdeiaComErroDeFuncaoDeUsuario() {
        ideia.getUsuarios().add(usuarioOutro); // Adiciona um usuário que não é colaborador

        assertThrows(BusinnesException.class, () -> ideiasService.save(ideia));


    }

    @Test
    @DisplayName("Salvar ideia com erro de usuário já vinculado a outra ideia")
    void salvarIdeiaComErroUsuarioJaVinculado() {
        usuarioColaborador.setIdeia(new IdeiasEntity()); // Usuário já vinculado a outra ideia
        ideia.getUsuarios().add(usuarioColaborador);

        assertThrows(BusinnesException.class, () -> ideiasService.save(ideia));


    }

    @Test
    @DisplayName("Encontrar ideia por ID com sucesso")
    void encontrarIdeiaPorIdComSucesso() {
        IdeiasEntity resultado = ideiasService.findById(1L);

        assertEquals(1L, resultado.getId());

    }

    @Test
    @DisplayName("Erro ao encontrar ideia por ID")
    void erroEncontrarIdeiaPorId() {
        Mockito.when(ideiasRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ideiasService.findById(2L));
    }

    @Test
    @DisplayName("Encontrar todas as ideias")
    void encontrarTodasIdeias() {
        List<IdeiasEntity> resultado = ideiasService.findAll();

        assertEquals(1, resultado.size());

    }

    @Test
    @DisplayName("Encontrar ideias por ID de evento com sucesso")
    void encontrarIdeiasPorEventoId() {

        Mockito.when(ideiasRepository.findByEventoId(1L)).thenReturn(Optional.of(List.of(ideia)));

        List<IdeiasEntity> resultado = ideiasService.findByEventoId(1L);
        assertEquals(1, resultado.size());

    }

    @Test
    @DisplayName("Erro ao encontrar ideias por ID de evento")
    void erroEncontrarIdeiasPorEventoId() {

        Mockito.when(ideiasRepository.findByEventoId(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ideiasService.findByEventoId(2L));
    }
}
