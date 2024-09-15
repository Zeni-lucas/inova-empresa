package com.inova_evento.app.services;

import com.inova_evento.app.entities.EventosEntity;
import com.inova_evento.app.entities.IdeiasEntity;
import com.inova_evento.app.entities.UsuariosEntity;
import com.inova_evento.app.entities.VotosPopularesEntity;
import com.inova_evento.app.exception.BusinnesException;
import com.inova_evento.app.exception.EntityNotFoundException;
import com.inova_evento.app.repositories.EventosRepository;
import com.inova_evento.app.repositories.IdeiasRepository;
import com.inova_evento.app.repositories.UsuariosRepository;
import com.inova_evento.app.repositories.VotosPopularesRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VotosPopularesServiceTest {

    @MockBean
    private VotosPopularesRepository votosPopularesRepository;

    @MockBean
    private EventosRepository eventosRepository;

    @MockBean
    private IdeiasRepository ideiasRepository;

    @MockBean
    private UsuariosRepository usuariosRepository;

    @Autowired
    private VotosPopularesService votosPopularesService;

    private IdeiasEntity ideia;
    private UsuariosEntity usuario;
    private EventosEntity evento;
    private VotosPopularesEntity votoPopular;

    @BeforeEach
    void setup() {
        // Criação do evento
        evento = new EventosEntity();
        evento.setDataInicio(LocalDate.now().minusDays(1));
        evento.setDataAvaliacaoPopular(LocalDate.now().plusDays(1));

        ideia = new IdeiasEntity();
        ideia.setId(1L);
        ideia.setEvento(evento);

        usuario = new UsuariosEntity();
        usuario.setId(1L);
        usuario.setNome("Usuário Teste");

        votoPopular = new VotosPopularesEntity();
        votoPopular.setUsuario(usuario);
        votoPopular.setIdeia(ideia);

        Mockito.when(ideiasRepository.findById(1L)).thenReturn(Optional.of(ideia));
        Mockito.when(usuariosRepository.findById(1L)).thenReturn(Optional.of(usuario));
        Mockito.when(votosPopularesRepository.findAll()).thenReturn(List.of());
    }

    @Test
    @DisplayName("Teste votar com sucesso")
    void votarComSucesso() {
        String resultado = votosPopularesService.votar(1L, 1L);

        assertEquals("Voto registrado com sucesso.", resultado);

    }

    @Test
    @DisplayName("Teste votar fora do período de votação")
    void votarForaDoPeriodo() {
        evento.setDataAvaliacaoPopular(LocalDate.now().minusDays(1));

        assertThrows(BusinnesException.class, () -> votosPopularesService.votar(1L, 1L));

    }

    @Test
    @DisplayName("Teste votar em uma ideia já votada pelo usuário")
    void votarDuplicado() {
        Mockito.when(votosPopularesRepository.findAll()).thenReturn(List.of(votoPopular));

        assertThrows(BusinnesException.class, () -> votosPopularesService.votar(1L, 1L));
    }

    @Test
    @DisplayName("Teste votar com ideia não encontrada")
    void votarIdeiaNaoEncontrada() {
        Mockito.when(ideiasRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> votosPopularesService.votar(1L, 2L));
    }

    @Test
    @DisplayName("Teste votar com usuário não encontrado")
    void votarUsuarioNaoEncontrado() {
        Mockito.when(usuariosRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> votosPopularesService.votar(2L, 1L));
    }
}
