package com.inova_evento.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "eventos")
public class EventosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDate dataAvaliacaoJurados;
    private LocalDate dataAvaliacaoPopular;

    @ManyToOne
    private UsuariosEntity usuario;

    @OneToMany(mappedBy = "evento")
    private List<IdeiasEntity> ideias;
}
