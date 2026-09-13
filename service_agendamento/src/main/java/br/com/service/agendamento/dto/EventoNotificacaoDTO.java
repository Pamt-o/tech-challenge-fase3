package br.com.service.agendamento.dto;

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