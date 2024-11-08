package com.ebitware.users.service;

import com.ebitware.users.exception.EmailExistsException;
import com.ebitware.users.exception.UsuarioNotFoundException;
import com.ebitware.users.model.entity.Usuario;
import com.ebitware.users.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new EmailExistsException(usuario.getEmail());
        }
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario existingUsuario = obtenerPorId(id);

        // Check if email is being changed and if new email already exists
        if (!existingUsuario.getEmail().equals(usuario.getEmail()) &&
                usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new EmailExistsException(usuario.getEmail());
        }

        existingUsuario.setNombre(usuario.getNombre());
        existingUsuario.setEmail(usuario.getEmail());
        return usuarioRepository.save(existingUsuario);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException(id);
        }
        usuarioRepository.deleteById(id);
    }
}