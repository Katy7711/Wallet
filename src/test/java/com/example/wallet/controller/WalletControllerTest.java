package com.example.wallet.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.wallet.OperationType;
import com.example.wallet.RequestState;
import com.example.wallet.controller.WalletController.WalletResponse;
import com.example.wallet.entity.User;
import com.example.wallet.entity.Wallet;
import com.example.wallet.service.WalletService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@WebMvcTest(WalletController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class WalletControllerTest {

  private final MockMvc mockMvc;
  @MockBean
  private WalletService walletService;

  @Autowired
  WalletControllerTest(WebApplicationContext webApplicationContext) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void depositOrWithdraw() throws Exception {
    User user = new User();
    user.setId(1L);
    user.setLogin("Иван");
    Wallet wallet = new Wallet();
    wallet.setRequestId(UUID.fromString("72984cdc-56fc-4c5a-b778-4e29b9a48ada"));
    wallet.setUser(user);
    wallet.setWalletAmount(3000.);
    String requestId = "72984cdc-56fc-4c5a-b778-4e29b9a48ada";
    OperationType type = OperationType.DEPOSIT;
    Double amount = 3000.;
    when(walletService.wallet(requestId, type, amount)).thenReturn(
        new WalletResponse(RequestState.SUCCESS, String.valueOf(wallet.getRequestId()),
            OperationType.DEPOSIT, 3000., wallet.getWalletAmount()));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet")
            .contentType(MediaType.APPLICATION_JSON)
            .param("requestId", requestId)
            .param("type", String.valueOf(type))
            .param("amount", String.valueOf(amount))
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
    verify(walletService, times(1)).wallet(requestId, type, amount);

  }

  @Test
  void getWalletAmount() throws Exception {
    User user = new User();
    user.setId(1L);
    user.setLogin("Иван");
    Wallet wallet = new Wallet();
    wallet.setRequestId(UUID.fromString("72984cdc-56fc-4c5a-b778-4e29b9a48ada"));
    wallet.setUser(user);
    wallet.setWalletAmount(3000.);
    String requestId = "72984cdc-56fc-4c5a-b778-4e29b9a48ada";
    when(walletService.getWalletAmount(requestId)).thenReturn(wallet.getWalletAmount());

    mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/wallet/72984cdc-56fc-4c5a-b778-4e29b9a48ada"))
        .andDo(print())
        .andExpect(status().isOk());
    verify(walletService, times(1)).getWalletAmount(anyString());

  }
}
