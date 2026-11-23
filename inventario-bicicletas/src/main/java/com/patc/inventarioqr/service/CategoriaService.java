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

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        return mapear(obtenerCategoria(id));
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        return guardar(new Categoria(), request);
    }

    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        return guardar(obtenerCategoria(id), request);
    }

    private Categoria obtenerCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
    }

    private CategoriaResponse guardar(Categoria categoria, CategoriaRequest request) {
        if (request == null || request.nombre() == null || request.nombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la categoría es obligatorio");
        }
        String nombre = request.nombre().strip();
        if (nombre.length() > 80 || (request.descripcion() != null && request.descripcion().length() > 200)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El nombre admite hasta 80 caracteres y la descripción hasta 200");
        }
        if (categoriaRepository.findByNombreIgnoreCase(nombre)
                .filter(otra -> !otra.getId().equals(categoria.getId())).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una categoría con ese nombre");
        }
        categoria.setNombre(nombre);
        categoria.setDescripcion(request.descripcion());
        return mapear(categoriaRepository.save(categoria));
    }

    private CategoriaResponse mapear(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNombre(), categoria.getDescripcion(), categoria.isActiva());
    }
}
