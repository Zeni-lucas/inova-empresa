package com.inova_evento.app.services;

import com.inova_evento.app.entities.UsuariosEntity;
import com.inova_evento.app.entities.enums.Roles;
import com.inova_evento.app.exception.UniqueEmailException;
import com.inova_evento.app.repositories.UsuariosRepository;
import org.junit.jupiter.api.Assertions;
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

@SpringBootTest
public class UsuarioServiceTest {

    @MockBean
    UsuariosRepository usuariosRepository;

    @Autowired
    UsuariosService usuariosService;

    UsuariosEntity usuario = new UsuariosEntity(null ,"mamonha cardoso", "mamonha302br@gmail.com", "mamonha123", Roles.ADMIN, null, null);
    UsuariosEntity usuarioAtualizado = new UsuariosEntity(null, "mamonha alterado", "mamonha302br@gmail.com", "mamonha123", Roles.ADMIN, null, null);


    @BeforeEach
    void setup(){
        UsuariosEntity usuarioReturn = new UsuariosEntity(10L,"mamonha cardoso", "mamonha302br@gmail.com", "mamonha123", Roles.ADMIN, null, null);
        Mockito.when(usuariosRepository.save(usuario)).thenReturn(usuarioReturn);
        var usuarioReturnAtualizado = new UsuariosEntity(10L, "mamonha alterado", "mamonha302br@gmail.com", "mamonha123", Roles.ADMIN, null, null);
        Mockito.when(usuariosRepository.save(usuarioAtualizado)).thenReturn(usuarioReturnAtualizado);
        Mockito.when(usuariosRepository.findById(10L)).thenReturn(Optional.of(usuarioReturn));
        Mockito.when(usuariosRepository.findById(usuarioAtualizado.getId())).thenReturn(Optional.of(usuarioAtualizado));


    }


    @Test
    @DisplayName("Teste save usuario")
    void cenario01(){
        var teste = usuariosService.save(usuario);
        Assertions.assertEquals(10, teste.getId());
        Assertions.assertEquals("mamonha cardoso", teste.getNome());
    }

    @Test
    @DisplayName("Teste update Usuario")
    void cenario02(){

        var testeUpdate = usuariosService.update(usuarioAtualizado, 10L);
        Assertions.assertEquals(10, testeUpdate.getId());
        Assertions.assertEquals("mamonha alterado", testeUpdate.getNome());

    }

    @Test
    @DisplayName("Teste delete usuario")
    void cenario04() {
        Long id = usuario.getId();
        Mockito.when(usuariosRepository.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(usuariosRepository).deleteById(id);

        usuariosService.delete(id);

        Assertions.assertDoesNotThrow(() -> usuariosService.delete(id), "A exclusão deveria ter sido bem-sucedida.");
    }

    @Test
    @DisplayName("teste findById")
    void cenario05(){
        var testeFind = usuariosService.findById(usuarioAtualizado.getId());
        Assertions.assertEquals(usuarioAtualizado.getId(), testeFind.getId());
        Assertions.assertEquals(usuarioAtualizado.getNome(), testeFind.getNome());

    }

    @Test
    @DisplayName("Teste findAll")
    void cenario08() {
        List<UsuariosEntity> usuarios = Arrays.asList(
                new UsuariosEntity(1L, "Nome1", "email1@example.com", "senha1", Roles.COLABORADOR, null, null),
                new UsuariosEntity(2L, "Nome2", "email2@example.com", "senha2", Roles.ADMIN, null, null)
        );

        Mockito.when(usuariosRepository.findAll()).thenReturn(usuarios);

        List<UsuariosEntity> resultado = usuariosService.findALl();

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.size());
        Assertions.assertEquals("Nome1", resultado.get(0).getNome());
    }

    @Test
    @DisplayName("Teste setRole com acesso autorizado")
    void cenario09() {
        Long adminId = 1L;
        Long userId = 2L;

        var admin = new UsuariosEntity(adminId, "Admin", "admin@example.com", "senha", Roles.ADMIN, null, null);
        var usuario = new UsuariosEntity(userId, "Usuario", "user@example.com", "senha", Roles.COLABORADOR, null, null);
        var usuarioAtualizado = new UsuariosEntity(userId, "Usuario", "user@example.com", "senha", Roles.ADMIN, null, null);

        Mockito.when(usuariosRepository.findById(adminId)).thenReturn(Optional.of(admin));
        Mockito.when(usuariosRepository.findById(userId)).thenReturn(Optional.of(usuario));
        Mockito.when(usuariosRepository.save(usuarioAtualizado)).thenReturn(usuarioAtualizado);

        var resultado = usuariosService.setRole(adminId, usuarioAtualizado);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(Roles.ADMIN, resultado.getRole());
    }

    @Test
    @DisplayName("Teste atualizarInformacoes com e-mail já existente")
    void cenario12() {
        UsuariosEntity usuarioExistente = new UsuariosEntity(10L, "Nome", "email@antigo.com", "senha", Roles.COLABORADOR, null, null);
        UsuariosEntity novoUsuario = new UsuariosEntity(null, "Nome Novo", "email@novo.com", "novaSenha", Roles.COLABORADOR, null, null);

        Mockito.when(usuariosRepository.existsByEmail(novoUsuario.getEmail())).thenReturn(true);

        Assertions.assertThrows(UniqueEmailException.class, () -> usuariosService.atualizarInformacoes(usuarioExistente, novoUsuario),
                "Deveria ter lançado UniqueEmailException.");
    }



}
