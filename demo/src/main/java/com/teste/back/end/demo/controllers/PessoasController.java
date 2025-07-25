package com.teste.back.end.demo.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teste.back.end.demo.model.Pessoas;
import com.teste.back.end.demo.model.dto.FiltroDTO;
import com.teste.back.end.demo.model.dto.PessoaGastosDTO;
import com.teste.back.end.demo.model.dto.PessoaResumoDTO;
import com.teste.back.end.demo.service.PessoasService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pessoas")
@RequiredArgsConstructor
public class PessoasController {
    
    private final PessoasService pessoaService;

    @PostMapping
    public ResponseEntity<Pessoas> adicionarPessoa(@RequestBody Pessoas pessoa) {
        Pessoas pessoaSalva = pessoaService.salvar(pessoa);
        return new ResponseEntity<>(pessoaSalva, HttpStatus.CREATED);    
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoas> atualizarPessoa(@PathVariable Long id, @RequestBody Pessoas pessoaAtualizada) {
        if (!pessoaExiste(id)) {
            return ResponseEntity.notFound().build();
        }
        pessoaAtualizada.setId(id); // garante que o id está correto
        Pessoas pessoaSalva = pessoaService.salvar(pessoaAtualizada);
        return ResponseEntity.ok(pessoaSalva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable Long id) {
        if (!pessoaExiste(id)) {
            return ResponseEntity.notFound().build();
        }
        pessoaService.deletar(id);
        System.out.println("Pessoa com ID " + id + " deletada com sucesso.");
        return ResponseEntity.noContent().build(); 
    }

    @GetMapping
    public ResponseEntity<List<PessoaResumoDTO>> listarPessoasComResumo() {
        List<PessoaResumoDTO> lista = pessoaService.listarResumo();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/gastos")
    public ResponseEntity<List<PessoaGastosDTO>> buscarGastos(
        @RequestParam String nome,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        FiltroDTO filtro = new FiltroDTO();
        filtro.setNome(nome);
        filtro.setDataInicio(dataInicio);
        filtro.setDataFim(dataFim);

        List<PessoaGastosDTO> resultado = pessoaService.buscarPorNomeEPeriodo(filtro);
        return ResponseEntity.ok(resultado);
    }



    private boolean pessoaExiste(Long id) {
        return pessoaService.buscarPorId(id).isPresent();
    }


}
