package ru.skillbox.paymentservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.paymentservice.domain.Wallet;
import ru.skillbox.paymentservice.dto.ReplenishmentDto;
import ru.skillbox.paymentservice.exception.AlreadyExistsException;
import ru.skillbox.paymentservice.exception.EntityNotFoundException;
import ru.skillbox.paymentservice.service.WalletService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
public class WalletController {

    private final WalletService walletService;

    @Operation(summary = "Create an e-wallet", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/wallet")
    public ResponseEntity<Wallet> createWallet(HttpServletRequest request) throws AlreadyExistsException {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(walletService.createWallet(Long.valueOf(request.getHeader("id"))));
    }

    @Operation(summary = "Get balance", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/wallet/balance")
    public ResponseEntity<BigDecimal> getBalance(HttpServletRequest request) {

        return ResponseEntity.ok()
                .body(walletService.getBalance(Long.valueOf(request.getHeader("id"))));
    }

    @Operation(summary = "Replenish balance", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/wallet/balance/replenish")
    public ResponseEntity<BigDecimal> replenishBalance(HttpServletRequest request,
                                                       @Valid @RequestBody ReplenishmentDto replenishmentDto)
            throws EntityNotFoundException {

        walletService.replenishBalance(Long.valueOf(request.getHeader("id")), replenishmentDto);
        return ResponseEntity.ok()
                .body(walletService.getBalance(Long.valueOf(request.getHeader("id"))));
    }
}
