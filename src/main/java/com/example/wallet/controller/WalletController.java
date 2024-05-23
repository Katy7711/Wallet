package com.example.wallet.controller;

import com.example.wallet.OperationType;
import com.example.wallet.RequestState;
import com.example.wallet.entity.User;
import com.example.wallet.entity.Wallet;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class WalletController {

  private static final Logger log = LoggerFactory.getLogger(WalletController.class);
  private final WalletService walletService;


  @PostMapping("/wallet")
  public ResponseEntity<WalletResponse> depositOrWithdraw(String requestId, OperationType type,
      Double amount) {
    log.info("Request for {}", type);
    return ResponseEntity.ok(walletService.wallet(requestId, type, amount));
  }

  @PostMapping("/creation/wallet")
  public ResponseEntity<CreateWalletResponse> creationWallet(User user) {
    log.info("Request for creation wallet for {}", user);
    return ResponseEntity.ok(walletService.creationWallet(user));
  }

  @GetMapping("/wallet/{walletUUID}")
  public ResponseEntity<Double> getWalletAmount(@PathVariable String walletUUID) {
    log.info("Request for get amount of the wallet id {} ", walletUUID);
    return ResponseEntity.ok(walletService.getWalletAmount(walletUUID));
  }

  public record WalletResponse(RequestState requestState, String id, OperationType type,
                               Double amount, Double walletAmount) {

  }

  public record CreateWalletResponse(RequestState requestState, Wallet wallet) {

  }
}