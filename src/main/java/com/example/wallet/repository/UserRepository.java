package com.example.wallet.repository;
import com.example.wallet.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

  boolean existsByLogin(String login);



}
