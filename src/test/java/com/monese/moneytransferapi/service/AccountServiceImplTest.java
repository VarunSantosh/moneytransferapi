package com.monese.moneytransferapi.service;

import com.monese.moneytransferapi.exception.AccountNotFoundException;
import com.monese.moneytransferapi.model.Account;
import com.monese.moneytransferapi.repository.AccountRepository;
import com.monese.moneytransferapi.repository.TransactionRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository mockAccountRepository;

    @Mock
    private TransactionRepository mockTransactionRepository;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(mockAccountRepository, mockTransactionRepository);
    }

    @AfterEach
    void destroy() {
        accountService = null;
    }

    @Test
    void testGetAccountDetailsToReturnAccount() {
        Account account = new Account();
        account.setFirstName("TFirstName");
        account.setLastName("TLastName");
        account.setId(123L);
        account.setBalance(new BigDecimal(5000));
        account.setTransactions(new HashSet<>());

        when(mockAccountRepository.findById(123L)).thenReturn(Optional.of(account));
        assertEquals(account, accountService.getAccountDetails(123L));
        verify(mockAccountRepository, times(1)).findById(123L);
    }

    @Test
    void testGetAccountDetailsToThrowAccountNotFoundException() {
        when(mockAccountRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountDetails(100L));
        verify(mockAccountRepository, times(1)).findById(100L);
    }
}
