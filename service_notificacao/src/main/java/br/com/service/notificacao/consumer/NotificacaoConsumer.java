package br.com.service.notificacao.consumer;


import br.com.service.notificacao.dto.EventoNotificacaoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class NotificacaoConsumer {

    private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

    @RabbitListener(queues = "${rabbitmq.queue.name:notificacao.queue}")
    public void receberEvento(@Payload EventoNotificacaoDTO evento) {
        log.info("📩 Evento recebido | Consulta ID: {} | Ação: {}", evento.consultaId(), evento.acao());

        String emoji = switch (evento.acao()) {
            case "CRIADA" -> "📅";
            case "EDITADA" -> "✏️";
            case "CANCELADA" -> "❌";
            case "REALIZADA" -> "✅";
            default -> "ℹ️";
        };
        String titulo = String.format("%s LEMBRETE DE CONSULTA — %s", emoji, evento.acao());

        String mensagem = String.format(
                "%n╔═════════════════════════════════════════════════════════════╗%n" +
                        "║  %-60s║%n" +
                        "╠═════════════════════════════════════════════════════════════╣%n" +
                        "║  Paciente : %-48s║%n" +
                        "║  E-mail   : %-48s║%n" +
                        "║  Médico   : %-48s║%n" +
                        "║  Data     : %-48s║%n" +
                        "║  Status   : %-48s║%n" +
                        "╚═════════════════════════════════════════════════════════════╝",
                titulo,
                evento.pacienteNome(),
                evento.pacienteEmail(),
                evento.medicoNome() != null ? evento.medicoNome() : "—",
                evento.dataHora().format(DATA_FORMATTER),
                evento.status() != null ? evento.status() : "—"
        );
        log.info(mensagem);
    }
}