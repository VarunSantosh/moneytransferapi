package com.monese.moneytransferapi.service;

import com.monese.moneytransferapi.api.PingController;
import com.monese.moneytransferapi.exception.AccountNotFoundException;
import com.monese.moneytransferapi.exception.InsufficientBalanceException;
import com.monese.moneytransferapi.model.Account;
import com.monese.moneytransferapi.model.AccountTransferPair;
import com.monese.moneytransferapi.model.Transaction;
import com.monese.moneytransferapi.repository.AccountRepository;
import com.monese.moneytransferapi.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private static Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * This method is to get account based on accountId
     * @param accountId
     * @return
     * @throws AccountNotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public Account getAccountDetails(Long accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId).orElseThrow(() -> {
            log.error("No account found with account id: '{}'.", accountId);
            return new AccountNotFoundException("No account found with account id: '" + accountId + "'.");
        });
    }

    /**
     * This method is to transfer the money between accounts
     * @param fromAccountId
     * @param toAccountId
     * @param amount
     * @return
     * @throws AccountNotFoundException        if account doesnt exist
     * @throws InsufficientBalanceException    if source account doesn't have sufficient balance
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public AccountTransferPair transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) throws AccountNotFoundException, InsufficientBalanceException {

        //fetch source account if exists or else throw exception
        Account fromAccount = getAccountDetails(fromAccountId);

        //fetch destination account if exists or else throw exception
        Account toAccount = getAccountDetails(toAccountId);

        // check if account has sufficient balance.
        if(fromAccount.getBalance().compareTo(amount) < 0) {
            log.error("Account with account id: '{}' doesn't have sufficient balance for the transfer.", fromAccountId);
            throw new InsufficientBalanceException("Account with account id: " + fromAccountId + " doesn't have sufficient balance for the transfer.");
        }

        // creating transactionId
        Long transactionId = generateUniqueTransactionId();

        // creating a debit transaction record
        Transaction debitTransaction = new Transaction();
        debitTransaction.setTransactionId(transactionId);
        debitTransaction.setAccount(fromAccount);
        debitTransaction.setTransactionAmount(amount);
        debitTransaction.setTransactionType("Debit");
        transactionRepository.save(debitTransaction);

        // creating a credit transaction record
        Transaction creditTransaction = new Transaction();
        creditTransaction.setTransactionId(transactionId);
        creditTransaction.setAccount(toAccount);
        creditTransaction.setTransactionAmount(amount);
        creditTransaction.setTransactionType("Credit");
        transactionRepository.save(creditTransaction);

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        log.info("Money transfer sucessfully completed!");
        return new AccountTransferPair(transactionId, fromAccountId, toAccountId, amount);
    }

    /**
     * This method creates a unique transactionId
     * @return
     */
    private Long generateUniqueTransactionId()
    {
        long val;
        do
        {
            final UUID uid = UUID.randomUUID();
            final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
            buffer.putLong(uid.getLeastSignificantBits());
            buffer.putLong(uid.getMostSignificantBits());
            final BigInteger bi = new BigInteger(buffer.array());
            val = bi.longValue();
        }
        // making sure that the ID is in positive space, if its not simply repeat the process
        while (val < 0);
        return val;
    }
}
