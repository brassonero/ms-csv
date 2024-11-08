package com.ebitware.users.mapper;

import com.ebitware.users.model.dto.UsuarioRequest;
import com.ebitware.users.model.dto.UsuarioResponse;
import com.ebitware.users.model.dto.UsuarioUpdate;
import com.ebitware.users.model.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        return usuario;
    }

    public UsuarioResponse toDto(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail()
        );
    }

    public void updateEntityFromDTO(UsuarioUpdate update, Usuario usuario) {
        usuario.setNombre(update.getNombre());
        usuario.setEmail(update.getEmail());
    }
}