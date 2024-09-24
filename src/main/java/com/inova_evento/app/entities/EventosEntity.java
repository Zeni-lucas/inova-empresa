package com.inova_evento.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    private String nome;
    @NotBlank
    private String descricao;
    @NotNull
    private LocalDate dataInicio;
    @NotNull
    private LocalDate dataFim;
    @NotNull
    private LocalDate dataAvaliacaoJurados;
    @NotNull
    private LocalDate dataAvaliacaoPopular;

    @JsonIgnore
    @ManyToOne
    private UsuariosEntity usuario;

    @OneToMany(mappedBy = "evento")
    @JsonIgnore
    private List<IdeiasEntity> ideias;

    @ManyToMany(mappedBy = "eventos")
    private List<JuradosEntity> jurados;
}
