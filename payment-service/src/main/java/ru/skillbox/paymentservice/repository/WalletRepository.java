package ru.skillbox.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.paymentservice.domain.Wallet;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserId(Long userId);
}
