package com.inova_evento.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "avaliacoes")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class AvaliacoesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JsonIgnore
    @JoinColumn(nullable = false)
    private IdeiasEntity ideia;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private JuradosEntity jurado;


    @Column(nullable = false)
    @Min(3)
    @Max(10)
    private Integer nota;


}
