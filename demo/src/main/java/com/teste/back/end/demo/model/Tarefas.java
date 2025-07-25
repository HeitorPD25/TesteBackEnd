package com.teste.back.end.demo.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Tarefas {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O Título não pode ser nulo.")
    private String titulo;

    @NotBlank(message = "A descrição não pode ser nula.")
    private String descricao;

    private LocalDateTime prazo;

    @NotBlank(message = "O Departamento não pode ser nulo.")
    private String departamento;

    private String duracao;

    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = true)
    @JsonBackReference
    private Pessoas pessoa;

    private boolean status;

}
