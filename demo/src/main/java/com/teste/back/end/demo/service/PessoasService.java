package com.teste.back.end.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.teste.back.end.demo.model.Pessoas;
import com.teste.back.end.demo.model.Tarefas;
import com.teste.back.end.demo.model.dto.FiltroDTO;
import com.teste.back.end.demo.model.dto.PessoaGastosDTO;
import com.teste.back.end.demo.model.dto.PessoaResumoDTO;
import com.teste.back.end.demo.repository.PessoasRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PessoasService {

    private final PessoasRepository pessoasRepository;

    public List<Pessoas> listarTodas() {
        return pessoasRepository.findAll();
    }

    public Optional<Pessoas> buscarPorId(Long id) {
        return pessoasRepository.findById(id);
    }

    public Pessoas salvar(Pessoas pessoa) {
        return pessoasRepository.save(pessoa);
    }

    public void deletar(Long id) {
        pessoasRepository.deleteById(id);
    }

    public List<PessoaResumoDTO> listarResumo() {
        return pessoasRepository.findAll().stream().map(pessoa -> {
            int totalHoras = 0;
            if (pessoa.getListaDeTarefas() != null) {
                for (var tarefa : pessoa.getListaDeTarefas()) {
                    String duracao = tarefa.getDuracao(); // ex: "2h" ou "1h30"
                    if (duracao != null && duracao.contains("h")) {
                        try {
                            String horasStr = duracao.split("h")[0];
                            totalHoras += Integer.parseInt(horasStr);
                        } catch (Exception e) {
                            // Ignorar se formato inválido
                        }
                    }
                }
            }
            return new PessoaResumoDTO(pessoa.getNome(), pessoa.getDepartamento(), totalHoras);
        }).collect(Collectors.toList());
    }

    public List<PessoaGastosDTO> buscarPorNomeEPeriodo(FiltroDTO dto) {
    String nome = dto.getNome() != null ? dto.getNome().trim() : "";

    LocalDate dataInicial = dto.getDataInicio();
    LocalDate dataFinal = dto.getDataFim();

    if (dataInicial == null || dataFinal == null) {
        throw new IllegalArgumentException("Data inicial e final devem ser informadas.");
    }

    List<Pessoas> pessoas = pessoasRepository.findByNomeContainingIgnoreCase(nome);

    return pessoas.stream()
        .map(pessoa -> {
            List<Tarefas> tarefasNoPeriodo = pessoa.getListaDeTarefas().stream()
                .filter(tarefa -> {
                    LocalDate dataPrazo = tarefa.getPrazo().toLocalDate();
                    return !dataPrazo.isBefore(dataInicial) && !dataPrazo.isAfter(dataFinal);
                })
                .collect(Collectors.toList());

            double mediaHoras = tarefasNoPeriodo.isEmpty() ? 0 :
                    tarefasNoPeriodo.stream()
                        .mapToDouble(t -> {
                            String duracao = t.getDuracao().replace("h", "").trim();
                            return Double.parseDouble(duracao);
                        })
                        .average()
                        .orElse(0);

            return new PessoaGastosDTO(pessoa.getNome(), mediaHoras);
        })
        .collect(Collectors.toList());
    }




}
