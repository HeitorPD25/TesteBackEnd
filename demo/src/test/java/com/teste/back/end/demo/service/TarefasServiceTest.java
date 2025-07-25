package com.teste.back.end.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.teste.back.end.demo.model.Tarefas;
import com.teste.back.end.demo.repository.TarefasRepository;

@ExtendWith(MockitoExtension.class)
public class TarefasServiceTest {

    @Mock
    private TarefasRepository tarefaRepository; // Usando apenas uma instância (remova a duplicação no service)

    @InjectMocks
    private TarefasService tarefasService;

    // ---- TESTE 1: listarTodas() ----
    @Test
    void testListarTodasAsTarefas() {
        // Configuração
        Tarefas tarefa1 = new Tarefas();
        Tarefas tarefa2 = new Tarefas();
        when(tarefaRepository.findAll()).thenReturn(Arrays.asList(tarefa1, tarefa2));

        // Execução
        List<Tarefas> resultado = tarefasService.listarTodas();

        // Verificação
        assertEquals(2, resultado.size());
        verify(tarefaRepository, times(1)).findAll();
    }

    // ---- TESTE 2: buscarPorId() ----
    @Test
    void testBuscarPorIdExistente() {
        // Configuração
        Tarefas tarefa = new Tarefas();
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));

        // Execução
        Optional<Tarefas> resultado = tarefasService.buscarPorId(1L);

        // Verificação
        assertTrue(resultado.isPresent());
    }

    @Test
    void testBuscarPorIdInexistente() {
        when(tarefaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Tarefas> resultado = tarefasService.buscarPorId(999L);

        assertTrue(resultado.isEmpty());
    }

    // ---- TESTE 3: salvar() ----
    @Test
    void testSalvarTarefa() {
        Tarefas tarefa = new Tarefas();
        tarefa.setTitulo("Reunião");

        when(tarefaRepository.save(tarefa)).thenReturn(tarefa);

        Tarefas resultado = tarefasService.salvar(tarefa);

        assertEquals("Reunião", resultado.getTitulo());
        verify(tarefaRepository, times(1)).save(tarefa);
    }

    // ---- TESTE 4: deletar() ----
    @Test
    void testDeletarTarefa() {
        // Configuração
        Long id = 1L;
        doNothing().when(tarefaRepository).deleteById(id);

        // Execução
        tarefasService.deletar(id);

        // Verificação
        verify(tarefaRepository, times(1)).deleteById(id);
    }

    // ---- TESTE 5: listarTarefasPendentes() ----
    @Test
    void testListarTarefasPendentes() {
        // Configuração
        Pageable pageable = PageRequest.of(0, 3);
        Tarefas tarefa1 = new Tarefas();
        Tarefas tarefa2 = new Tarefas();
        when(tarefaRepository.findByPessoaIsNullOrderByPrazoAsc(pageable))
            .thenReturn(Arrays.asList(tarefa1, tarefa2));

        // Execução
        List<Tarefas> resultado = tarefasService.listarTarefasPendentes();

        // Verificação
        assertEquals(2, resultado.size());
        verify(tarefaRepository, times(1)).findByPessoaIsNullOrderByPrazoAsc(pageable);
    }
}