package br.com.service.agendamento.producer;

import br.com.service.agendamento.config.RabbitMQConfig;
import br.com.service.agendamento.dto.EventoNotificacaoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultaProducer {

    private final RabbitTemplate rabbitTemplate;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void enviarEventoCriacao(EventoNotificacaoDTO evento) {
        enviar(evento, RabbitMQConfig.ROUTING_KEY_CREATED);
    }

    public void enviarEventoEdicao(EventoNotificacaoDTO evento) {
        enviar(evento, RabbitMQConfig.ROUTING_KEY_UPDATED);
    }

    private void enviar(EventoNotificacaoDTO evento, String routingKey) {
        String emoji = getEmoji(evento.acao());

        log.info("{} Publicando evento | Consulta ID: {} | Ação: {} | Paciente: {} | Data: {} | RoutingKey: {}",
                emoji,
                evento.consultaId(),
                evento.acao(),
                evento.pacienteNome(),
                evento.dataHora().format(FORMATTER),
                routingKey
        );

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, routingKey, evento);

        log.info("✅ Evento publicado com sucesso | Consulta ID: {}", evento.consultaId());
    }

    private String getEmoji(String acao) {
        return switch (acao) {
            case "CRIADA" -> "📅";
            case "EDITADA" -> "✏️";
            case "CANCELADA" -> "❌";
            case "REALIZADA" -> "✅";
            default -> "📤";
        };
    }
}
