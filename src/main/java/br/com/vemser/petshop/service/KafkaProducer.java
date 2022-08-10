package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.pedido.PedidoDTOConsumer;
import br.com.vemser.petshop.entity.PedidoEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    @Value("${kafka.balanco-topic}")
    private String topicoBalanco;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public void enviarMensagemKafka(String mensagem, String topico) {
        MessageBuilder<String> stringMessageBuilder = MessageBuilder.withPayload(mensagem)
                .setHeader(KafkaHeaders.TOPIC, topico)
                .setHeader(KafkaHeaders.MESSAGE_KEY, UUID.randomUUID().toString());
        Message<String> stringMessage = stringMessageBuilder.build();

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(stringMessage);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("Erro ao enviar mensagem: '{}', para o Kafka", mensagem);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Mensagem: '{}', enviada para o Kafka com sucesso", mensagem);
            }
        });
    }

    public void sendPedido(PedidoDTOConsumer pedidoDTOConsumer) throws JsonProcessingException {
        String mensagemFinal = objectMapper.writeValueAsString(pedidoDTOConsumer);
        enviarMensagemKafka(mensagemFinal, topicoBalanco);
    }
}
