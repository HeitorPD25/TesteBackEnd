package com.teste.back.end.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teste.back.end.demo.model.Pessoas;

public interface PessoasRepository extends JpaRepository<Pessoas, Long> {

    List<Pessoas> findByNomeContainingIgnoreCase(String nome);
}

