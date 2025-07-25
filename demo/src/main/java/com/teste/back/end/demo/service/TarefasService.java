package com.teste.back.end.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import com.teste.back.end.demo.model.Tarefas;
import com.teste.back.end.demo.repository.TarefasRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefaRepository;

    public List<Tarefas> listarTodas() {
        return tarefaRepository.findAll();
    }

    public Optional<Tarefas> buscarPorId(Long id) {
        return tarefaRepository.findById(id);
    }

    public Tarefas salvar(Tarefas tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public void deletar(Long id) {
        tarefaRepository.deleteById(id);
    }

    public List<Tarefas> listarTarefasPendentes() {
        Pageable topThreeByPrazoAsc = PageRequest.of(0, 3);
        return tarefaRepository.findByPessoaIsNullOrderByPrazoAsc(topThreeByPrazoAsc);
    }
}
