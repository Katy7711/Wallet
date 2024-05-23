package com.example.wallet.controller;

import com.example.wallet.RequestState;
import com.example.wallet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);
  private final UserService userService;


  @PostMapping("/creation/user")
  public ResponseEntity<UserResponse> creationUser(String login) {
    log.info("Request for creation user {}", login);
    return ResponseEntity.ok(userService.creationUser(login));
  }

  public record UserResponse(RequestState requestState, String login) {


  }
}