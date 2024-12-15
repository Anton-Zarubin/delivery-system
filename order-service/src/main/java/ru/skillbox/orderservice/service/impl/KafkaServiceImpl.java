package ru.skillbox.orderservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.orderservice.dto.PaymentKafkaDto;
import ru.skillbox.orderservice.service.KafkaService;

@Service
public class KafkaServiceImpl implements KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Value("${spring.kafka.payment-service-topic}")
    private String kafkaTopic;

    private final KafkaTemplate<Long, PaymentKafkaDto> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<Long, PaymentKafkaDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(PaymentKafkaDto paymentKafkaDto) {
        kafkaTemplate.send(kafkaTopic, paymentKafkaDto);
        logger.info("Sent message to Kafka -> '{}'", paymentKafkaDto);
    }
}
