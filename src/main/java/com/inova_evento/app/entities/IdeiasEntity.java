package com.inova_evento.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ideias")
public class IdeiasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nomeIdeia;
    @NotBlank
    private String impactoIdeia;
    @NotNull
    private BigDecimal custoImplantacao;
    @NotBlank
    @Column(length = 1000)
    @Size(min = 1, max = 1000)
    private String descricao;

    @NotNull
    @OneToMany(mappedBy = "ideia")
    @JsonIgnore
    private List<UsuariosEntity> usuarios;
    @NotNull
    @ManyToOne
    @JsonIgnore
    private EventosEntity evento;

    @OneToMany(mappedBy = "ideia")
    private List<AvaliacoesEntity> avaliacoes;

    @OneToMany(mappedBy = "ideia")
    private List<VotosPopularesEntity> votos;

}
