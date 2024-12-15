package ru.skillbox.paymentservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skillbox.paymentservice.dto.ErrorKafkaDto;
import ru.skillbox.paymentservice.service.KafkaService;

@Service
public class KafkaServiceImpl implements KafkaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Value("${spring.kafka.error-order-service-topic}")
    private String kafkaErrorOrderServiceTopic;

    @Value("${spring.kafka.order-service-topic}")
    private String kafkaOrderServiceTopic;

    private final KafkaTemplate<Long, Object> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<Long, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(Object kafkaDto) {
        if (kafkaDto instanceof ErrorKafkaDto) {
            kafkaTemplate.send(kafkaErrorOrderServiceTopic, kafkaDto);
        } else {
            kafkaTemplate.send(kafkaOrderServiceTopic, kafkaDto);
        }
        LOGGER.info("Sent message to Kafka -> '{}'", kafkaDto);
    }
}
