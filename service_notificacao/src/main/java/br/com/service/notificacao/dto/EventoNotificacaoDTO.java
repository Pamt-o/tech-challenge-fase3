package br.com.service.notificacao.dto;


import java.time.LocalDateTime;

public record EventoNotificacaoDTO(
        Long consultaId,
        String pacienteEmail,
        String pacienteNome,
        String medicoNome,
        LocalDateTime dataHora,
        String acao, // "CRIADA" ou "EDITADA"
        String status
) {}