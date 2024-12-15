package ru.skillbox.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.paymentservice.domain.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);
}
