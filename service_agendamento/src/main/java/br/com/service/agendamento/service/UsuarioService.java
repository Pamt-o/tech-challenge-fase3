package br.com.service.agendamento.service;

import br.com.service.agendamento.dto.request.UsuarioRequest;
import br.com.service.agendamento.dto.response.UsuarioResponse;
import br.com.service.agendamento.entity.Usuario;
import br.com.service.agendamento.exception.BusinessException;
import br.com.service.agendamento.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Listar todos os usuários
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioResponse::fromEntity)
                .toList();
    }

    //Listar usuários por role
    public List<UsuarioResponse> listarPorRole(String role) {
        Usuario.Role roleEnum;
        try {
            roleEnum = Usuario.Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Perfil inválido. Use: MEDICO, ENFERMEIRO ou PACIENTE");
        }
        return usuarioRepository.findByRole(roleEnum).stream()
                .map(UsuarioResponse::fromEntity)
                .toList();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado com ID: " + id));
    }


    public Usuario cadastrar(UsuarioRequest request) {
        if (usuarioRepository.findByUsername(request.username()).isPresent()) {
            throw new BusinessException("Username já está em uso: " + request.username());
        }

        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessException("Email já está cadastrado: " + request.email());
        }

        Usuario.Role role;
        try {
            role = Usuario.Role.valueOf(request.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Perfil inválido. Use: MEDICO, ENFERMEIRO ou PACIENTE");
        }

        Usuario usuario = Usuario.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .enabled(true)
                .nome(request.nome())
                .email(request.email())
                .role(role)
                .build();

        return usuarioRepository.save(usuario);
    }
}