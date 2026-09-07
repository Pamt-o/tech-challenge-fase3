package br.com.service.agendamento.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "consulta.exchange";
    public static final String QUEUE_NAME = "notificacao.queue";
    public static final String ROUTING_KEY_CREATED = "consulta.criada";
    public static final String ROUTING_KEY_UPDATED = "consulta.editada";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true); // durável
    }

    @Bean
    public Binding bindingCreated() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(ROUTING_KEY_CREATED);
    }

    @Bean
    public Binding bindingUpdated() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(ROUTING_KEY_UPDATED);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
