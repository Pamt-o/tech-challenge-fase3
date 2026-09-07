package br.com.service.agendamento.dto.request;

public record UsuarioRequest(
        String username,
        String password,
        String nome,
        String email,
        String role // MEDICO, ENFERMEIRO, PACIENTE
) {}
