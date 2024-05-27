package com.example.wallet.service;

import static com.example.wallet.OperationType.DEPOSIT;
import static com.example.wallet.OperationType.WITHDRAW;

import com.example.wallet.OperationType;
import com.example.wallet.RequestState;
import com.example.wallet.controller.WalletController.CreateWalletResponse;
import com.example.wallet.controller.WalletController.WalletResponse;
import com.example.wallet.entity.User;
import com.example.wallet.entity.Wallet;
import com.example.wallet.repository.UserRepository;
import com.example.wallet.repository.WalletRepository;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.webjars.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
@Transactional
public class WalletService {

  private static final Logger log = LoggerFactory.getLogger(WalletService.class);
  private final WalletRepository walletRepository;
  private final UserRepository userRepository;

  @Autowired
  public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
    this.walletRepository = walletRepository;
    this.userRepository = userRepository;
  }


  @Transactional
  public WalletResponse wallet(String requestId, OperationType type, Double amount) {
    log.info("Was invoked method for {} by wallet id {}", type, requestId);
    Wallet wallet = walletRepository.findByRequestId(UUID.fromString(requestId))
        .orElseThrow(() -> new NotFoundException("Кошелек с id " + requestId + " не существует!")
        );
//      log.warn("Wallet with {} not found  ", requestId);
    if (type == DEPOSIT) {
      if (wallet.getWalletAmount() == null) {
        wallet.setWalletAmount(amount);
      } else {
        wallet.setWalletAmount(wallet.getWalletAmount() + amount);
      }
      if (wallet.getDepositAmount() == null) {
        wallet.setDepositAmount(amount);
      } else {
        wallet.setDepositAmount(wallet.getDepositAmount() + amount);
        walletRepository.save(wallet);
      }
      log.info("Deposit {} completed successfully, wallet {}", amount, requestId);
    }

    if (type == WITHDRAW) {
      if (wallet.getWalletAmount() == null || wallet.getWalletAmount() < amount) {
        throw new RuntimeException("на счету недостаточно средств для снятия");
      }
      log.warn("There are not enough funds in a wallet {} ", requestId);
      wallet.setWalletAmount(wallet.getWalletAmount() - amount);
      if (wallet.getWithdrawAmount() == null) {
        wallet.setWithdrawAmount(amount);
      } else {
        wallet.setWithdrawAmount(wallet.getWithdrawAmount() + amount);
      }
      walletRepository.save(wallet);
      log.info("Withdraw {} completed successfully, wallet {}", amount, requestId);
    }
    return new WalletResponse(RequestState.SUCCESS, requestId, type, amount,
        wallet.getWalletAmount());
  }

  public CreateWalletResponse creationWallet(User user) {
    if (!userRepository.existsByLogin(user.getLogin())) {
      log.warn("user not exists");
      throw new NotFoundException(
          String.format("Пользователь \"%s\" не существует!", user.getLogin()));
    }
    UUID uuid = UUID.randomUUID();
    Wallet wallet = new Wallet();
    wallet.setRequestId(uuid);
    wallet.setUser(user);
    wallet.setWalletCreationDate(new Timestamp(new Date().getTime()));
    walletRepository.save(wallet);
    log.info("Wallet for {} create successfully", user);
    return new CreateWalletResponse(RequestState.SUCCESS, wallet);
  }

  public Double getWalletAmount(String walletUUID) {
    Wallet wallet = walletRepository.findByRequestId(UUID.fromString(walletUUID))
        .orElseThrow(() -> new NotFoundException("Кошелек с id " + walletUUID + " не существует!"));
    return wallet.getWalletAmount();
  }
}
