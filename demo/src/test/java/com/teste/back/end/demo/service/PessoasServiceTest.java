package com.teste.back.end.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import com.teste.back.end.demo.model.Pessoas;
import com.teste.back.end.demo.model.Tarefas;
import com.teste.back.end.demo.model.dto.FiltroDTO;
import com.teste.back.end.demo.model.dto.PessoaGastosDTO;
import com.teste.back.end.demo.model.dto.PessoaResumoDTO;
import com.teste.back.end.demo.repository.PessoasRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PessoasServiceTest {

    @Mock
    private PessoasRepository pessoasRepository;

    @InjectMocks
    private PessoasService pessoasService;

    // ---- TESTE 1: listarResumo() ----
    @Test
    void testListarResumoComTarefasValidas() {
        // Configuração
        Pessoas pessoa = new Pessoas();
        pessoa.setNome("Maria");
        pessoa.setDepartamento("RH");

        Tarefas tarefa1 = new Tarefas();
        tarefa1.setDuracao("2h"); // 2 horas
        Tarefas tarefa2 = new Tarefas();
        tarefa2.setDuracao("1h30"); // 1 hora (ignora os minutos no seu código)
        pessoa.setListaDeTarefas(Arrays.asList(tarefa1, tarefa2));

        when(pessoasRepository.findAll()).thenReturn(Arrays.asList(pessoa));

        // Execução
        List<PessoaResumoDTO> resultado = pessoasService.listarResumo();

        // Verificação
        assertEquals(1, resultado.size());
        assertEquals(3, resultado.get(0).getTotalHoras()); // 2h + 1h = 3h
    }

    // ---- TESTE 2: buscarPorNomeEPeriodo() ----
    @Test
    void testBuscarPorNomeEPeriodoComDatasValidas() {
        // Configuração
        FiltroDTO filtro = new FiltroDTO("João", 
            LocalDate.of(2023, 1, 1), 
            LocalDate.of(2023, 12, 31));

        Pessoas pessoa = new Pessoas();
        pessoa.setNome("João");

        Tarefas tarefa = new Tarefas();
        tarefa.setPrazo(LocalDate.of(2023, 6, 15).atStartOfDay());
        tarefa.setDuracao("5h");
        pessoa.setListaDeTarefas(Arrays.asList(tarefa));

        when(pessoasRepository.findByNomeContainingIgnoreCase("João"))
            .thenReturn(Arrays.asList(pessoa));

        // Execução
        List<PessoaGastosDTO> resultado = pessoasService.buscarPorNomeEPeriodo(filtro);

        // Verificação
        assertEquals(1, resultado.size());
        assertEquals(5.0, resultado.get(0).getMediaHorasPorTarefa());
    }

    // ---- TESTE 3: buscarPorNomeEPeriodo() com datas nulas ----
    @Test
    void testBuscarPorNomeEPeriodoComDatasNulas() {
        FiltroDTO filtro = new FiltroDTO("João", null, null);

        assertThrows(IllegalArgumentException.class, () -> {
            pessoasService.buscarPorNomeEPeriodo(filtro);
        });
    }

    // ---- TESTE 4: salvar() ----
    @Test
    void testSalvarPessoa() {
        Pessoas pessoa = new Pessoas();
        pessoa.setNome("Carlos");

        when(pessoasRepository.save(pessoa)).thenReturn(pessoa);

        Pessoas resultado = pessoasService.salvar(pessoa);

        assertEquals("Carlos", resultado.getNome());
        verify(pessoasRepository, times(1)).save(pessoa);
    }
}