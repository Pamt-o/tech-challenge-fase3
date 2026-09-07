package br.com.service.notificacao.consumer;


import br.com.service.notificacao.dto.EventoNotificacaoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificacaoConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name:notificacao.queue}")
    public void receberEvento(@Payload EventoNotificacaoDTO evento) {
        log.info("Mensagem recebida do RabbitMQ: Consulta ID {}", evento.consultaId());

        String mensagem = String.format(
                "LEMBRETE ENVIADO para %s (%s) sobre a consulta ID %d em %s. Ação: %s",
                evento.pacienteNome(),
                evento.pacienteEmail(),
                evento.consultaId(),
                evento.dataHora(),
                evento.acao()
        );
        log.info(mensagem);
    }
}