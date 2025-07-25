package com.teste.back.end.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.teste.back.end.demo.model.Tarefas;

public interface TarefasRepository extends JpaRepository<Tarefas, Long> {

    List<Tarefas> findByDepartamento(String departamento);

    List<Tarefas> findByStatus(boolean status);

    List<Tarefas> findByPessoaId(Long pessoaId);

    List<Tarefas> findByPessoaIsNullOrderByPrazoAsc(Pageable pageable);

}

