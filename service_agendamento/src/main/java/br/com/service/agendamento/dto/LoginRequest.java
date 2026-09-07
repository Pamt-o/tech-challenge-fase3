package br.com.service.agendamento.dto;

public record LoginRequest(
        String username,
        String password
) {}