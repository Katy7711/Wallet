package com.example.wallet.repository;

import com.example.wallet.entity.Wallet;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Integer> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Wallet> findByRequestId(UUID requestId);

}
