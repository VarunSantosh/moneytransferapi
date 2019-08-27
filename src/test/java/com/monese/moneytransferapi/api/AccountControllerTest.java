package com.monese.moneytransferapi.api;

import com.monese.moneytransferapi.exception.AccountNotFoundException;
import com.monese.moneytransferapi.exception.InsufficientBalanceException;
import com.monese.moneytransferapi.model.Account;
import com.monese.moneytransferapi.model.AccountTransferPair;
import com.monese.moneytransferapi.service.AccountService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService mockAccountService;

    private AccountController controller;

    @BeforeEach
    void setUp(){
        controller = new AccountController(mockAccountService);
    }

    @AfterEach
    void destroy() {
        controller = null;
    }

    /**
     * This test is to check the successful return of account statement for a valid accountId
     */
    @Test
    void testAccountStatementResponseToBe200_OK() {
        Account account = new Account();
        account.setFirstName("TFirstName");
        account.setLastName("TLastName");
        account.setId(123L);
        account.setBalance(new BigDecimal(5000));
        account.setTransactions(new HashSet<>());

        when(mockAccountService.getAccountDetails(123L)).thenReturn(account);

        ResponseEntity response = controller.accountStatement(123L);
        verify(mockAccountService, times(1)).getAccountDetails(123L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    /**
     * This test is to check the failure to return of account statement for a invalid accountId
     */
    @Test
    void testAccountStatementResponseToBe404_NOT_FOUND() {

        when(mockAccountService.getAccountDetails(100L)).thenThrow(new AccountNotFoundException("No account found with account id: '100'."));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> controller.accountStatement(100L));
        verify(mockAccountService, times(1)).getAccountDetails(100L);
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    /**
     * This test is to check the successful transfer of money from valid account with valid balance
     */
    @Test
    void testTransferMoneyResponseToBe201_CREATED() {
        AccountTransferPair acPair = new AccountTransferPair(123456L, 101L, 102L, new BigDecimal(500));

        when(mockAccountService.transfer(101L, 102L, new BigDecimal(500))).thenReturn(acPair);

        ResponseEntity response = controller.transferMoney(101L, 102L, new BigDecimal(500));
        verify(mockAccountService, times(1)).transfer(101L, 102L, new BigDecimal(500));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(acPair, response.getBody());
    }

    /**
     * This test is to check for transfer of money failure because of invalid source account
     */
    @Test
    void testTransferMoneyResponseToBe404_NOT_FOUND() {

        when(mockAccountService.transfer(101L, 102L, new BigDecimal(500))).thenThrow(new AccountNotFoundException("No account found with account id: '101'."));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> controller.transferMoney(101L, 102L, new BigDecimal(500)));
        verify(mockAccountService, times(1)).transfer(101L, 102L, new BigDecimal(500));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    /**
     * This test is to check for transfer of money failure because of insufficient balance in source account
     */
    @Test
    void testTransferMoneyResponseToBe400_BAD_REQUEST_ForInsufficientBalance() {

        when(mockAccountService.transfer(101L, 102L, new BigDecimal(500))).thenThrow(new InsufficientBalanceException("Account with account id: '101' doesn't have sufficient balance for the transfer."));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> controller.transferMoney(101L, 102L, new BigDecimal(500)));
        verify(mockAccountService, times(1)).transfer(101L, 102L, new BigDecimal(500));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    /**
     * This test is to check for transfer of money failure because source and destination account is same
     */
    @Test
    void testTransferMoneyResponseToBe400_BAD_REQUEST_ForToAndFromAccountsToBeSame() {

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> controller.transferMoney(101L, 101L, new BigDecimal(500)));
        verify(mockAccountService, times(0)).transfer(101L, 101L, new BigDecimal(500));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }
}
