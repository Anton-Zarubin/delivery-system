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
import ru.skillbox.paymentservice.dto.ReplenishmentDto;
import ru.skillbox.paymentservice.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfig.class)
public class WalletServiceTest {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    Wallet wallet;

    @BeforeEach
    public void setUp() {
        wallet = new Wallet();
        wallet.setId(1L);
        wallet.setUserId(1L);
        wallet.setBalance(BigDecimal.TEN);
    }

    @Test
    void createWallet() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> walletService.createWallet(1L));
    }

    @Test
    void whenWalletAlreadyExists_thanException() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.ofNullable(wallet));
        assertThrows(RuntimeException.class, () -> walletService.createWallet(1L));
    }

    @Test
    void replenishBalance() {
        ReplenishmentDto replenishmentDto = new ReplenishmentDto(BigDecimal.ONE);
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.ofNullable(wallet));
        assertEquals(new BigDecimal(11), walletService.replenishBalance(1L, replenishmentDto));
    }

    @Test
    void whenWalletNotFound_thanReplenishmentIsFailed() {
        ReplenishmentDto replenishmentDto = new ReplenishmentDto(BigDecimal.ONE);
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> walletService.replenishBalance(1L,replenishmentDto));
    }

    @Test
    void getBalance() {
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.ofNullable(wallet));
        assertEquals(BigDecimal.TEN, walletService.getBalance(1L));
    }
}
