package br.com.service.notificacao.dto;


import java.time.LocalDateTime;

public record EventoNotificacaoDTO(
        Long consultaId,
        String pacienteEmail,
        String pacienteNome,
        LocalDateTime dataHora,
        String acao
) {}