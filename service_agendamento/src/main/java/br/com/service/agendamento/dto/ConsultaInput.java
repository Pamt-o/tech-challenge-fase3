package br.com.service.agendamento.dto;

public record ConsultaInput(
        Long pacienteId,
        Long medicoId,
        String dataHora, // ISO-8601 (ex: "2026-09-10T14:30:00")
        String observacoes
) {}