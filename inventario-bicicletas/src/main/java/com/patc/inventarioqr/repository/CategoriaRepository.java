package com.patc.inventarioqr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patc.inventarioqr.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombreIgnoreCase(String nombre);
}
