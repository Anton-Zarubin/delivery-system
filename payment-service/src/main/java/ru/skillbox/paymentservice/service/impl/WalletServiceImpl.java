package ru.skillbox.paymentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.paymentservice.domain.Wallet;
import ru.skillbox.paymentservice.dto.ReplenishmentDto;
import ru.skillbox.paymentservice.exception.AlreadyExistsException;
import ru.skillbox.paymentservice.exception.EntityNotFoundException;
import ru.skillbox.paymentservice.repository.WalletRepository;
import ru.skillbox.paymentservice.service.WalletService;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    public Wallet createWallet(Long userId) throws AlreadyExistsException {
        Optional<Wallet> optionalWallet = walletRepository.findByUserId(userId);
        if (optionalWallet.isPresent()) {
            throw new AlreadyExistsException(MessageFormat.format("Wallet for user with id {0} already exists",
                    userId));
        }

        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(BigDecimal.ZERO);
        return walletRepository.save(wallet);
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        Optional<Wallet> optionalWallet = walletRepository.findByUserId(userId);
        if (optionalWallet.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Wallet for user with id {0} not found",
                    userId));
        }

        Wallet wallet = optionalWallet.get();
        return wallet.getBalance();
    }

    @Override
    public BigDecimal replenishBalance(Long userId, ReplenishmentDto replenishmentDto) {
        Optional<Wallet> optionalWallet = walletRepository.findByUserId(userId);
        if (optionalWallet.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Wallet for user with id {0} not found",
                    userId));
        }

        Wallet wallet = optionalWallet.get();
        wallet.setBalance(wallet.getBalance().add(replenishmentDto.getAmount()));
        walletRepository.save(wallet);

        return wallet.getBalance();
    }
}
