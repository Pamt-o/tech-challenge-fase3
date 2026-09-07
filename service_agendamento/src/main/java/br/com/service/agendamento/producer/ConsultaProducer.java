package br.com.service.agendamento.producer;

import br.com.service.agendamento.config.RabbitMQConfig;
import br.com.service.agendamento.dto.EventoNotificacaoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultaProducer {

    private final RabbitTemplate rabbitTemplate;

    public void enviarEventoCriacao(EventoNotificacaoDTO evento) {
        enviar(evento, RabbitMQConfig.ROUTING_KEY_CREATED);
    }

    public void enviarEventoEdicao(EventoNotificacaoDTO evento) {
        enviar(evento, RabbitMQConfig.ROUTING_KEY_UPDATED);
    }

    private void enviar(EventoNotificacaoDTO evento, String routingKey) {
        log.info("Publicando evento no RabbitMQ: Consulta ID {} | routingKey: {}",
                evento.consultaId(), routingKey);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, routingKey, evento);
    }
}
