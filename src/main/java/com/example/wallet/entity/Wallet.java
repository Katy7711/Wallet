package com.example.wallet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.Data;

@Entity
@Data
@Table(name = "wallet")
public class Wallet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;
  UUID requestId;
  @OneToOne
  User user;
  Timestamp walletCreationDate;
  Double walletAmount;
  Double depositAmount;
  Double withdrawAmount;

}
