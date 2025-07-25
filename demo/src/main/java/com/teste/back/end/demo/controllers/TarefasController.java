package com.teste.back.end.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.back.end.demo.model.Pessoas;
import com.teste.back.end.demo.model.Tarefas;
import com.teste.back.end.demo.service.PessoasService;
import com.teste.back.end.demo.service.TarefasService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefasController {

    private final TarefasService tarefaService;

    private final PessoasService pessoaService;
    @PostMapping
    public ResponseEntity<Tarefas> adicionarTarefa(@RequestBody Tarefas tarefa) {
        Tarefas tarefaSalva = tarefaService.salvar(tarefa);
        return new ResponseEntity<>(tarefaSalva, HttpStatus.CREATED);
    }

    @PutMapping("/alocar/{id}")
    public ResponseEntity<?> alocarPessoaNaTarefa(@PathVariable Long id, @RequestBody Pessoas pessoa) {
        var tarefaOpt = tarefaService.buscarPorId(id);
        if (tarefaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var tarefa = tarefaOpt.get();

        if (!pessoaService.buscarPorId(pessoa.getId()).isPresent()) {
            return ResponseEntity.badRequest().body("Pessoa não encontrada");
        }

        var pessoaEncontrada = pessoaService.buscarPorId(pessoa.getId()).get();

        if (!pessoaEncontrada.getDepartamento().equalsIgnoreCase(tarefa.getDepartamento())) {
            return ResponseEntity.badRequest().body("Departamento da pessoa não corresponde ao da tarefa");
        }

        tarefa.setPessoa(pessoaEncontrada);
        Tarefas tarefaAtualizada = tarefaService.salvar(tarefa);

        return ResponseEntity.ok(tarefaAtualizada);
    }

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<Tarefas> finalizarTarefa(@PathVariable Long id) {
        var tarefaOpt = tarefaService.buscarPorId(id);

        if (tarefaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var tarefa = tarefaOpt.get();
        tarefa.setStatus(true);
        Tarefas tarefaFinalizada = tarefaService.salvar(tarefa);

        return ResponseEntity.ok(tarefaFinalizada);
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<Tarefas>> listarTarefasPendentes() {
        List<Tarefas> tarefasPendentes = tarefaService.listarTarefasPendentes();
        return ResponseEntity.ok(tarefasPendentes);
    }

}
