package ru.skillbox.paymentservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.skillbox.paymentservice.repository.PaymentRepository;
import ru.skillbox.paymentservice.repository.WalletRepository;
import ru.skillbox.paymentservice.service.KafkaService;
import ru.skillbox.paymentservice.service.PaymentService;
import ru.skillbox.paymentservice.service.WalletService;
import ru.skillbox.paymentservice.service.impl.PaymentServiceImpl;
import ru.skillbox.paymentservice.service.impl.WalletServiceImpl;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public KafkaService kafkaServiceMock() {
        return mock(KafkaService.class);
    }

    @Bean
    public WalletRepository walletRepositoryMock(){
        return mock(WalletRepository.class);
    }

    @Bean
    public PaymentRepository paymentRepositoryMock() {
        return mock(PaymentRepository.class);
    }

    @Bean
    public WalletService walletService() {
        return new WalletServiceImpl(walletRepositoryMock());
    }

    @Bean
    public PaymentService paymentService() {
        return new PaymentServiceImpl(walletRepositoryMock(), paymentRepositoryMock(),
                kafkaServiceMock());
    }
}
