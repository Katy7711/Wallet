package com.example.wallet.service;

import static com.example.wallet.OperationType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.wallet.OperationType;
import com.example.wallet.controller.WalletController.CreateWalletResponse;
import com.example.wallet.controller.WalletController.WalletResponse;
import com.example.wallet.entity.User;
import com.example.wallet.entity.Wallet;
import com.example.wallet.repository.UserRepository;
import com.example.wallet.repository.WalletRepository;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {


  @Mock
  private WalletRepository walletRepository;
  @Mock
  private UserRepository userRepository;
  @InjectMocks
  private WalletService walletService;


  @Test
  void creationWallet() {
    User user = new User();
    user.setLogin("Иван");
    when(userRepository.existsByLogin(any())).thenReturn(true);
    CreateWalletResponse createWalletResponse = walletService.creationWallet(user);
    Wallet wallet = createWalletResponse.wallet();
    assertEquals(user, wallet.getUser());
    verify(walletRepository, times(1)).save(any(Wallet.class));
  }

  @Test
  void wallet() {
    User user = new User();
    user.setLogin("Иван");
    when(userRepository.existsByLogin(any())).thenReturn(true);
    CreateWalletResponse createWalletResponse = walletService.creationWallet(user);
    Wallet wallet = createWalletResponse.wallet();
    when(walletRepository.findByRequestId(any())).thenReturn(Optional.ofNullable(wallet));
    WalletResponse walletResponse = walletService.wallet(String.valueOf(
            Objects.requireNonNull(wallet).getRequestId()),
        DEPOSIT, 1000.);
    assertEquals(1000, walletResponse.walletAmount());
    WalletResponse walletResponse1 = walletService.wallet(String.valueOf(
            Objects.requireNonNull(wallet).getRequestId()),
        WITHDRAW, 1000.);
    assertEquals(0, walletResponse1.walletAmount());
    Assertions.assertThrows(RuntimeException.class, () ->
      walletService.wallet(String.valueOf(
              Objects.requireNonNull(wallet).getRequestId()),
          WITHDRAW, 1000.)
    );
    verify(walletRepository, times(2)).save(any(Wallet.class));
  }

  @Test
  void getWalletAmount() {
    Wallet wallet = new Wallet();
    wallet.setId(1);
    wallet.setRequestId(UUID.fromString("72984cdc-56fc-4c5a-b778-4e29b9a48ada"));
    wallet.setUser(new User());
    wallet.setWalletAmount(3000.);
    wallet.setDepositAmount(3000.);
    when(walletRepository.findByRequestId(any())).thenReturn(Optional.of(wallet));
    walletService.getWalletAmount(String.valueOf(wallet.getRequestId()));
    assertEquals(3000., wallet.getWalletAmount());
    verify(walletRepository, times(1)).findByRequestId(any(UUID.class));
  }

  @Test
  void wallet_Multithreading()  {
    class MyThread extends Thread {
      final String id;
      final OperationType type;
      final Double amount;

      public MyThread(String id, OperationType type, double amount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
      }

      @Override
      public void run() {
          walletService.wallet(id,type,amount);
      }
    }
    User user = new User();
    user.setLogin("Иван");
    when(userRepository.existsByLogin(any())).thenReturn(true);
    CreateWalletResponse createWalletResponse = walletService.creationWallet(user);
    Wallet wallet = createWalletResponse.wallet();
    when(walletRepository.findByRequestId(any())).thenReturn(Optional.ofNullable(wallet));
    for(int i=0; i<50; i++) {
      MyThread thread = new MyThread(String.valueOf(
          Objects.requireNonNull(wallet).getRequestId()),
          OperationType.DEPOSIT, 1.);
      thread.start();
    }
    assertEquals(50, wallet.getWalletAmount());
  }
}
