package com.teste.back.end.demo.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.back.end.demo.model.Pessoas;
import com.teste.back.end.demo.model.dto.PessoaGastosDTO;
import com.teste.back.end.demo.model.dto.PessoaResumoDTO;
import com.teste.back.end.demo.service.PessoasService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class PessoasControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PessoasService pessoaService;

    @InjectMocks
    private PessoasController pessoasController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(pessoasController).build();
    }

    // ---- TESTE 1: POST /pessoas ----
    @Test
    void deveRetornar201_AoAdicionarPessoa() throws Exception {
        Pessoas pessoa = new Pessoas();
        pessoa.setNome("Ana");
        when(pessoaService.salvar(any())).thenReturn(pessoa);

        mockMvc.perform(MockMvcRequestBuilders.post("/pessoas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Ana"));
    }

    // ---- TESTE 2: PUT /pessoas/{id} (sucesso) ----
    @Test
    void deveRetornar200_AoAtualizarPessoaExistente() throws Exception {
        Pessoas pessoa = new Pessoas();
        pessoa.setId(1L);
        when(pessoaService.buscarPorId(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaService.salvar(any())).thenReturn(pessoa);

        mockMvc.perform(MockMvcRequestBuilders.put("/pessoas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isOk());
    }

    // ---- TESTE 3: PUT /pessoas/{id} (não encontrado) ----
    @Test
    void deveRetornar404_AoAtualizarPessoaInexistente() throws Exception {
        when(pessoaService.buscarPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/pessoas/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Inexistente\"}"))
                .andExpect(status().isNotFound());
    }

    // ---- TESTE 4: DELETE /pessoas/{id} ----
    @Test
    void deveRetornar204_AoDeletarPessoaExistente() throws Exception {
        when(pessoaService.buscarPorId(1L)).thenReturn(Optional.of(new Pessoas()));
        doNothing().when(pessoaService).deletar(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/pessoas/1"))
                .andExpect(status().isNoContent());
    }

    // ---- TESTE 5: GET /pessoas ----
    @Test
    void deveRetornarListaDePessoas() throws Exception {
        when(pessoaService.listarResumo()).thenReturn(List.of(
            new PessoaResumoDTO("Carlos", "RH", 8)
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/pessoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Carlos"));
    }

    // ---- TESTE 6: GET /pessoas/gastos (sucesso) ----
    @Test
    void deveRetornarGastosPorPeriodo() throws Exception {
        when(pessoaService.buscarPorNomeEPeriodo(any()))
            .thenReturn(List.of(new PessoaGastosDTO("Maria", 3.5)));

        mockMvc.perform(MockMvcRequestBuilders.get("/pessoas/gastos")
                .param("nome", "Maria")
                .param("dataInicio", "2023-01-01")
                .param("dataFim", "2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mediaHorasPorTarefa").value(3.5));
    }

    // ---- TESTE 7: GET /pessoas/gastos (datas inválidas) ----
    @Test
    void deveRetornar400_QuandoDatasInvalidas() throws Exception {
        when(pessoaService.buscarPorNomeEPeriodo(any()))
            .thenThrow(new IllegalArgumentException());

        mockMvc.perform(MockMvcRequestBuilders.get("/pessoas/gastos")
                .param("nome", "Maria"))
                .andExpect(status().isBadRequest());
    }
}