package com.patc.inventarioqr.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.patc.inventarioqr.dto.CategoriaRequest;
import com.patc.inventarioqr.dto.CategoriaResponse;
import com.patc.inventarioqr.model.Categoria;
import com.patc.inventarioqr.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream().map(this::mapear).toList();
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        if (request == null || request.nombre() == null || request.nombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la categoria es obligatorio");
        }
        categoriaRepository.findByNombreIgnoreCase(request.nombre().strip()).ifPresent(c -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una categoria con ese nombre");
        });

        Categoria categoria = new Categoria(request.nombre().strip(), request.descripcion());
        return mapear(categoriaRepository.save(categoria));
    }

    private CategoriaResponse mapear(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNombre(), categoria.getDescripcion(), categoria.isActiva());
    }
}
