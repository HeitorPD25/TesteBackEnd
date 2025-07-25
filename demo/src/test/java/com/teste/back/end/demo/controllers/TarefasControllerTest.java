package com.teste.back.end.demo.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.back.end.demo.model.Pessoas;
import com.teste.back.end.demo.model.Tarefas;
import com.teste.back.end.demo.service.PessoasService;
import com.teste.back.end.demo.service.TarefasService;

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
public class TarefasControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TarefasService tarefaService; // Usando apenas uma instância

    @Mock
    private PessoasService pessoaService;

    @InjectMocks
    private TarefasController tarefasController;

    @BeforeEach
    void setup() {
        // Corrigindo a injeção duplicada no controller
        tarefasController = new TarefasController(tarefaService, pessoaService);
        mockMvc = MockMvcBuilders.standaloneSetup(tarefasController).build();
    }

    // ---- TESTE 1: POST /tarefas ----
    @Test
    void deveRetornar201_AoCriarTarefa() throws Exception {
        Tarefas tarefa = new Tarefas();
        tarefa.setTitulo("Reunião");
        when(tarefaService.salvar(any())).thenReturn(tarefa);

        mockMvc.perform(MockMvcRequestBuilders.post("/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarefa)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Reunião"));
    }

    // ---- TESTE 2: PUT /tarefas/alocar/{id} (sucesso) ----
    @Test
    void deveRetornar200_AoAlocarPessoa() throws Exception {
        Tarefas tarefa = new Tarefas();
        tarefa.setId(1L);
        tarefa.setDepartamento("TI");
        
        Pessoas pessoa = new Pessoas();
        pessoa.setId(1L);
        pessoa.setDepartamento("TI");

        when(tarefaService.buscarPorId(1L)).thenReturn(Optional.of(tarefa));
        when(pessoaService.buscarPorId(1L)).thenReturn(Optional.of(pessoa));
        when(tarefaService.salvar(any())).thenReturn(tarefa);

        mockMvc.perform(MockMvcRequestBuilders.put("/tarefas/alocar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isOk());
    }

    // ---- TESTE 3: PUT /tarefas/alocar/{id} (tarefa não encontrada) ----
    @Test
    void deveRetornar404_AoAlocarPessoaEmTarefaInexistente() throws Exception {
        when(tarefaService.buscarPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/tarefas/alocar/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1}"))
                .andExpect(status().isNotFound());
    }

    // ---- TESTE 4: PUT /tarefas/alocar/{id} (departamentos diferentes) ----
    @Test
    void deveRetornar400_AoAlocarPessoaDeDepartamentoDiferente() throws Exception {
        Tarefas tarefa = new Tarefas();
        tarefa.setId(1L);
        tarefa.setDepartamento("TI");
        
        Pessoas pessoa = new Pessoas();
        pessoa.setId(1L);
        pessoa.setDepartamento("RH");

        when(tarefaService.buscarPorId(1L)).thenReturn(Optional.of(tarefa));
        when(pessoaService.buscarPorId(1L)).thenReturn(Optional.of(pessoa));

        mockMvc.perform(MockMvcRequestBuilders.put("/tarefas/alocar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Departamento da pessoa não corresponde ao da tarefa"));
    }

    // ---- TESTE 5: PUT /tarefas/finalizar/{id} ----
    @Test
    void deveRetornar200_AoFinalizarTarefa() throws Exception {
        Tarefas tarefa = new Tarefas();
        tarefa.setId(1L);
        when(tarefaService.buscarPorId(1L)).thenReturn(Optional.of(tarefa));
        when(tarefaService.salvar(any())).thenReturn(tarefa);

        mockMvc.perform(MockMvcRequestBuilders.put("/tarefas/finalizar/1"))
                .andExpect(status().isOk());
    }

    // ---- TESTE 6: GET /tarefas/pendentes ----
    @Test
    void deveRetornarListaDeTarefasPendentes() throws Exception {
        when(tarefaService.listarTarefasPendentes())
            .thenReturn(List.of(new Tarefas()));

        mockMvc.perform(MockMvcRequestBuilders.get("/tarefas/pendentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
