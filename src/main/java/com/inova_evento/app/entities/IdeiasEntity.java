package com.inova_evento.app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String descricao;

    @NotNull
    @OneToMany(mappedBy = "ideia")
    private List<UsuariosEntity> usuarios;
    @NotNull
    @ManyToOne
    private EventosEntity evento;
}
