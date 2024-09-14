package com.inova_evento.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "jurados")
public class JuradosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UsuariosEntity usuario;

    @ManyToMany
    @JoinTable(
            name = "jurado_eventos",
            joinColumns = @JoinColumn(name = "jurado_id"),
            inverseJoinColumns = @JoinColumn(name = "evento_id")
    )
    private List<EventosEntity> eventos;

    @OneToMany(mappedBy = "jurado")
    List<AvaliacoesEntity> avaliacoes;
}
