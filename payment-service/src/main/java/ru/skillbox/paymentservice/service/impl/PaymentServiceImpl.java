package ru.skillbox.paymentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.paymentservice.domain.Payment;
import ru.skillbox.paymentservice.domain.Wallet;
import ru.skillbox.paymentservice.dto.*;
import ru.skillbox.paymentservice.exception.InsufficientFundsException;
import ru.skillbox.paymentservice.exception.EntityNotFoundException;
import ru.skillbox.paymentservice.repository.PaymentRepository;
import ru.skillbox.paymentservice.repository.WalletRepository;
import ru.skillbox.paymentservice.service.KafkaService;
import ru.skillbox.paymentservice.service.PaymentService;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final WalletRepository walletRepository;

    private final PaymentRepository paymentRepository;

    private final KafkaService kafkaService;

    @Transactional
    @Override
    public void pay(PaymentKafkaDto paymentKafkaDto) {
        try {
            Thread.sleep(3000);
            Long userId = paymentKafkaDto.getUserId();
            Long orderId = paymentKafkaDto.getOrderId();
            BigDecimal cost = paymentKafkaDto.getCost();

            Optional<Wallet> optionalWallet = walletRepository.findByUserId(userId);
            if (optionalWallet.isEmpty()) {
                String comment = MessageFormat.format("Wallet for user with id {0} not found",
                        userId);
                StatusDto statusDto = createStatusDto(OrderStatus.PAYMENT_FAILED, comment);
                kafkaService.produce(new ErrorKafkaDto(orderId, statusDto));

                throw new EntityNotFoundException(comment);
            }

            Wallet wallet = optionalWallet.get();
            if (wallet.getBalance().compareTo(cost) < 0) {
                String comment = "Insufficient funds";
                StatusDto statusDto = createStatusDto(OrderStatus.PAYMENT_FAILED, comment);
                kafkaService.produce(new ErrorKafkaDto(orderId, statusDto));

                throw new InsufficientFundsException(comment);
            }

            recordFactOfPayment(orderId, cost, wallet);

            String comment = "Order paid";
            StatusDto statusDto = createStatusDto(OrderStatus.PAID, comment);
            kafkaService.produce(new OrderKafkaDto(orderId, statusDto));

        } catch (Exception ex) {
            if (!(ex instanceof EntityNotFoundException) && !(ex instanceof InsufficientFundsException)) {
                StatusDto statusDto = createStatusDto(OrderStatus.UNEXPECTED_FAILURE, ex.getMessage());
                kafkaService.produce(new ErrorKafkaDto(paymentKafkaDto.getOrderId(), statusDto));
            }

            throw new RuntimeException(ex.getMessage());
        }
    }

    private void recordFactOfPayment(Long orderId, BigDecimal cost, Wallet wallet) {
        wallet.setBalance(wallet.getBalance().subtract(cost));
        walletRepository.save(wallet);

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setCost(cost);
        payment.setWallet(wallet);
        paymentRepository.save(payment);
    }

    private StatusDto createStatusDto(OrderStatus orderStatus, String comment) {
        StatusDto statusDto = new StatusDto();
        statusDto.setStatus(orderStatus);
        statusDto.setServiceName(ServiceName.PAYMENT_SERVICE);
        statusDto.setComment(comment);

        return statusDto;
    }
}
