package ru.skillbox.paymentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.paymentservice.domain.Wallet;
import ru.skillbox.paymentservice.dto.ReplenishmentDto;
import ru.skillbox.paymentservice.exception.AlreadyExistsException;
import ru.skillbox.paymentservice.exception.ResourceNotFoundException;
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
        Optional<Wallet> optionalBalance = walletRepository.findByUserId(userId);
        if (optionalBalance.isPresent()) {
            throw new AlreadyExistsException(MessageFormat.format("An e-wallet for user with ID {0} already exists",
                    userId));
        }

        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(BigDecimal.ZERO);
        return walletRepository.save(wallet);
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        Optional<Wallet> optionalBalance = walletRepository.findByUserId(userId);
        if (optionalBalance.isEmpty()) {
            throw new ResourceNotFoundException(MessageFormat.format("The wallet for user with ID {0} not found", userId));
        }

        Wallet wallet = optionalBalance.get();
        return wallet.getBalance();
    }

    @Override
    public BigDecimal replenishBalance(Long userId, ReplenishmentDto replenishmentDto) {
        Optional<Wallet> optionalBalance = walletRepository.findByUserId(userId);
        if (optionalBalance.isEmpty()) {
            throw new ResourceNotFoundException(MessageFormat.format("The wallet for user with ID {0} not found", userId));
        }

        Wallet wallet = optionalBalance.get();
        wallet.setBalance(wallet.getBalance().add(replenishmentDto.getAmount()));
        walletRepository.save(wallet);

        return wallet.getBalance();
    }
}
