package com.ebitware.users.controller;

import com.ebitware.users.mapper.UsuarioMapper;
import com.ebitware.users.model.dto.UsuarioRequest;
import com.ebitware.users.model.dto.UsuarioResponse;
import com.ebitware.users.model.dto.UsuarioUpdate;
import com.ebitware.users.model.entity.Usuario;
import com.ebitware.users.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario = usuarioMapper.toEntity(request);
        Usuario nuevoUsuario = usuarioService.crear(usuario);
        return new ResponseEntity<>(usuarioMapper.toDto(nuevoUsuario), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> obtenerTodos() {
        List<UsuarioResponse> usuarios = usuarioService.obtenerTodos()
                .stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuarioMapper.toDto(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdate update) {
        Usuario existingUsuario = usuarioService.obtenerPorId(id);
        usuarioMapper.updateEntityFromDTO(update, existingUsuario);
        Usuario updatedUsuario = usuarioService.actualizar(id, existingUsuario);
        return ResponseEntity.ok(usuarioMapper.toDto(updatedUsuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}