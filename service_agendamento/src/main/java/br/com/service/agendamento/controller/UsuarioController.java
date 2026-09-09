package br.com.service.agendamento.controller;

import br.com.service.agendamento.dto.request.UsuarioRequest;
import br.com.service.agendamento.dto.response.UsuarioResponse;
import br.com.service.agendamento.entity.Usuario;
import br.com.service.agendamento.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> cadastrar(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario = usuarioService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    // 🔥 Listar todos os usuários (apenas para ADMIN ou Médicos)
    @GetMapping("/usuarios")
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // Listar usuários por perfil
    @GetMapping("/usuarios/role/{role}")
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMEIRO')")
    public ResponseEntity<List<UsuarioResponse>> listarUsuariosPorRole(@PathVariable String role) {
        List<UsuarioResponse> usuarios = usuarioService.listarPorRole(role);
        return ResponseEntity.ok(usuarios);
    }
}
