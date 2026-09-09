package br.com.service.agendamento.dto.response;

import br.com.service.agendamento.entity.Usuario;

public record UsuarioResponse(
                              Long id,
                              String username,
                              String nome,
                              String email,
                              String role
) {
    public static UsuarioResponse fromEntity(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole().name()
        );
    }
}
