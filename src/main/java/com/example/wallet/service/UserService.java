package com.example.wallet.service;

import com.example.wallet.RequestState;
import com.example.wallet.controller.UserController.UserResponse;
import com.example.wallet.entity.User;
import com.example.wallet.repository.UserRepository;
import jakarta.validation.ValidationException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
@Transactional
public class UserService {

  private static final Logger log = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  public UserResponse creationUser(String login) {
    if (userRepository.existsByLogin(login)) {
      log.warn("user already exists");
      throw new ValidationException(String.format("Пользователь \"%s\" уже существует!", login));
    }
    User user = new User();
    user.setLogin(login);
    userRepository.save(user);
    log.info("User {} created", login);
    return new UserResponse(RequestState.SUCCESS, login);
  }
}
