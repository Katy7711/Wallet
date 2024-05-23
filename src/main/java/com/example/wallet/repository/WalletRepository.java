package com.example.wallet.repository;

import com.example.wallet.entity.Wallet;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Integer> {

  Optional<Wallet> findByRequestId(UUID requestId);

}
