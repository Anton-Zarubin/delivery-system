package ru.skillbox.paymentservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.skillbox.paymentservice.config.TestConfig;
import ru.skillbox.paymentservice.domain.Wallet;
import ru.skillbox.paymentservice.dto.PaymentKafkaDto;
import ru.skillbox.paymentservice.repository.PaymentRepository;
import ru.skillbox.paymentservice.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    PaymentKafkaDto paymentKafkaDto;

    @BeforeEach
    public void setUp() {
        paymentKafkaDto = new PaymentKafkaDto();
        paymentKafkaDto.setUserId(1L);
        paymentKafkaDto.setOrderId(1L);
        paymentKafkaDto.setCost(BigDecimal.TEN);
        paymentKafkaDto.setDepartureAddress("some address");
        paymentKafkaDto.setDestinationAddress("some address");
    }

    @Test
    void pay() {
        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(BigDecimal.TEN);

        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(wallet));
        assertDoesNotThrow(() -> paymentService.pay(paymentKafkaDto));
        assertThat(wallet.getBalance()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void payWithException() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> paymentService.pay(paymentKafkaDto));
    }
}
