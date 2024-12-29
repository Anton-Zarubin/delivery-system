package ru.skillbox.paymentservice.service;

import ru.skillbox.paymentservice.domain.Wallet;
import ru.skillbox.paymentservice.dto.ReplenishmentDto;
import ru.skillbox.paymentservice.exception.AlreadyExistsException;

import java.math.BigDecimal;

public interface WalletService {

    Wallet createWallet(Long userId) throws AlreadyExistsException;

    BigDecimal getBalance(Long userId);

    BigDecimal replenishBalance(Long userId, ReplenishmentDto replenishmentDto);
}
