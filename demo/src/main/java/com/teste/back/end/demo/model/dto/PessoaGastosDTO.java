package com.teste.back.end.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PessoaGastosDTO {
    private String nome;
    private double mediaHorasPorTarefa;
}

