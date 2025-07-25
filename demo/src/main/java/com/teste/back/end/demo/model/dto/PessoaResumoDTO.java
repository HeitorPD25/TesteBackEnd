package com.teste.back.end.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PessoaResumoDTO {
    private String nome;
    private String departamento;
    private int totalHoras;
}
