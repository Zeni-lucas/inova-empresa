package com.inova_evento.app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "votos_populares")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class VotosPopularesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UsuariosEntity usuario;

    @ManyToOne
    private IdeiasEntity ideia;
}
