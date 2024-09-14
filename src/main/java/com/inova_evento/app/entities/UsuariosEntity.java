package com.inova_evento.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.inova_evento.app.entities.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "O nome deve ser informado")
    private String nome;
    @NotBlank @Email(message = "O email deve ter um formato valido")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "Uma senha deve ser informada")
    private String senha;

    @Enumerated(EnumType.STRING)
    private Roles role = Roles.COLABORADOR;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
    private List<EventosEntity> eventos;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    private IdeiasEntity ideia;

    @OneToMany(mappedBy = "usuario")
    List<JuradosEntity> jurados;

}
