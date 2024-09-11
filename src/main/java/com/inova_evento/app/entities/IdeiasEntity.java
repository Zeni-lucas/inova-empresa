package com.inova_evento.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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
    private String nomeIdeia;
    private String impactoIdeia;
    private BigDecimal custoImplantacao;
    private String descricao;

    @OneToOne
    private UsuariosEntity usuario;

    @ManyToOne
    private EventosEntity evento;
}
