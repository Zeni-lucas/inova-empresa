package com.inova_evento.app.services;


import com.inova_evento.app.entities.*;
import com.inova_evento.app.entities.enums.Roles;
import com.inova_evento.app.exception.AccessDeniedException;
import com.inova_evento.app.exception.BusinnesException;
import com.inova_evento.app.exception.EntityNotFoundException;
import com.inova_evento.app.repositories.AvaliacoesRepository;
import com.inova_evento.app.repositories.IdeiasRepository;
import com.inova_evento.app.repositories.JuradosRepository;
import com.inova_evento.app.repositories.UsuariosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@SpringBootTest
public class JuradosServiceTest {

    @MockBean
    private JuradosRepository juradosRepository;

    @MockBean
    private UsuariosRepository usuariosRepository;

    @MockBean
    private IdeiasRepository ideiasRepository;

    @MockBean
    private AvaliacoesRepository avaliacaoRepository;

    @Autowired
    private JuradosService juradosService;

    private UsuariosEntity admin;
    private UsuariosEntity colaborador;
    private JuradosEntity jurado;
    private IdeiasEntity ideia;

    @BeforeEach
    void setup() {
        UsuariosEntity usuario = new UsuariosEntity(1L, "Jurado", "jurado@gmail.com", "senha123",Roles.ADMIN, null, null, null, null);
        UsuariosEntity usuario1 = new UsuariosEntity(2L, "Jurado", "jurado@gmail.com", "senha123",Roles.ADMIN, null, null, null, null);
        admin = new UsuariosEntity(1L, "ADMIN", "admin@gmail.com", "senha123", Roles.ADMIN, null, null, null, null);
        colaborador = new UsuariosEntity(2L, "COLABORADOR", "colaborador@gmail.com", "senha123", Roles.COLABORADOR, null, null, null, null);

        jurado = new JuradosEntity(1L, usuario, null, null);
        ideia = new IdeiasEntity(1L, "Ideia Teste", "Descrição da Ideia", null,null,null,null,null,null);

        Mockito.when(usuariosRepository.findById(1L)).thenReturn(Optional.of(admin));
        Mockito.when(usuariosRepository.findById(2L)).thenReturn(Optional.of(colaborador));
        Mockito.when(juradosRepository.save(any(JuradosEntity.class))).thenReturn(jurado);
        Mockito.when(juradosRepository.findById(1L)).thenReturn(Optional.of(jurado));
        Mockito.when(ideiasRepository.findByEventoId(1L)).thenReturn(Optional.of(List.of(ideia)));
        Mockito.when(juradosRepository.findByEventoId(1L)).thenReturn(Optional.of(List.of(jurado, new JuradosEntity(2L, usuario1, null, null))));

    }

    @Test
    @DisplayName("Teste save jurado com usuário admin")
    void cenario01() {
        JuradosEntity result = juradosService.save(1L, jurado);

        assertEquals(jurado, result);
    }

    @Test
    @DisplayName("Teste save jurado com usuário colaborador (não autorizado)")
    void cenario02() {
        assertThrows(AccessDeniedException.class, () -> juradosService.save(2L, jurado));
    }

    @Test
    @DisplayName("Teste findById com sucesso")
    void cenario03() {
        JuradosEntity foundJurado = juradosService.findById(1L);

        assertEquals(1L, foundJurado.getId());
        assertEquals("Jurado", foundJurado.getUsuario().getNome());
    }

    @Test
    @DisplayName("Teste findById com erro")
    void cenario04() {
        Mockito.when(juradosRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> juradosService.findById(2L));
    }

    @Test
    @DisplayName("Teste distribuir ideias com sucesso")
    void cenario05() {
        juradosService.distribuirIdeias(1L);

        verify(avaliacaoRepository, times(2)).save(any(AvaliacoesEntity.class));
    }

    @Test
    @DisplayName("Teste distribuir ideias com menos de dois jurados")
    void cenario06() {
        Mockito.when(juradosRepository.findByEventoId(1L)).thenReturn(Optional.of(List.of(jurado)));

        assertThrows(BusinnesException.class, () -> juradosService.distribuirIdeias(1L));
    }
}
