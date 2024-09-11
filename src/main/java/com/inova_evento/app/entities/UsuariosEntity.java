package com.inova_evento.app.entities;

import com.inova_evento.app.entities.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class UsuariosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @OneToMany(mappedBy = "usuario")
    private List<EventosEntity> eventos;

    @OneToOne(mappedBy = "usuario")
    private IdeiasEntity ideia;

}
