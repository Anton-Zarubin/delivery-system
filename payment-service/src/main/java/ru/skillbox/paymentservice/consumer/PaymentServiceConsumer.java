package ru.skillbox.paymentservice.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.skillbox.paymentservice.dto.PaymentKafkaDto;
import ru.skillbox.paymentservice.service.PaymentService;

@Component
public class PaymentServiceConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceConsumer.class);

    private final PaymentService paymentService;

    @Autowired
    public PaymentServiceConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "${spring.kafka.payment-service-topic}")
    public void consume(PaymentKafkaDto paymentKafkaDto) {
        LOGGER.info("Consumed message from Kafka -> '{}'", paymentKafkaDto);
        paymentService.pay(paymentKafkaDto);
    }
}