package br.com.service.agendamento.dto;

public record LoginResponse(
        boolean success,
        String message,
        String token
) {
    // Construtor adicional para casos sem token (erro)
    public LoginResponse(boolean success, String message) {
        this(success, message, null);
    }
}
