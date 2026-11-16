package com.patc.inventarioqr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patc.inventarioqr.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreoIgnoreCase(String correo);
}
