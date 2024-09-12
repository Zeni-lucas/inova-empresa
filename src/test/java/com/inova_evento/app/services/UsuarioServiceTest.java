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

@SpringBootTest
public class UsuarioServiceTest {

    @MockBean
    UsuariosRepository usuariosRepository;

    @Autowired
    UsuariosService usuariosService;

    UsuariosEntity usuario = new UsuariosEntity(null ,"mamonha cardoso", "mamonha302br@gmail.com", "mamonha123", Roles.ADMIN, null, null);


    @BeforeEach
    void setup(){
        UsuariosEntity usuarioReturn = new UsuariosEntity(10L,"mamonha cardoso", "mamonha302br@gmail.com", "mamonha123", Roles.ADMIN, null, null);
        Mockito.when(usuariosRepository.save(usuario)).thenReturn(usuarioReturn);
    }


    @Test
    @DisplayName("Teste save usuario")
    void cenario01(){
        var teste = usuariosService.save(usuario);
        Assertions.assertEquals(10, teste.getId());
        Assertions.assertEquals("mamonha cardoso", teste.getNome());
        Assertions.assertThrows(UniqueEmailException.class, () ->  usuariosService.save(usuario));
    }

}
