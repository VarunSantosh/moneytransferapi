package com.monese.moneytransferapi.service;

import com.monese.moneytransferapi.exception.AccountNotFoundException;
import com.monese.moneytransferapi.exception.InsufficientBalanceException;
import com.monese.moneytransferapi.model.Account;
import com.monese.moneytransferapi.model.AccountTransferPair;

import java.math.BigDecimal;

public interface AccountService {

    /**
     * Retrieve account representation from db, throws AccountNotFoundException if not present
     */
    Account getAccountDetails(Long AccountId) throws AccountNotFoundException;

    /**
     * Transfer money amount between accounts, throws InsufficientFundsException if "from" account balance is not enough
     */
    AccountTransferPair transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) throws AccountNotFoundException, InsufficientBalanceException;
}
