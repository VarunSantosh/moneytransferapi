package com.monese.moneytransferapi.api;

import com.monese.moneytransferapi.exception.AccountNotFoundException;
import com.monese.moneytransferapi.exception.InsufficientBalanceException;
import com.monese.moneytransferapi.model.Account;
import com.monese.moneytransferapi.model.AccountTransferPair;
import com.monese.moneytransferapi.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/v1")
public class AccountController {

    private static Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * This api is to get the statement of an account based on accountId
     * @param accountId the account id
     * @return return 200OK if found else return 400 NOT FOUND
     */
    @GetMapping("/account/{id}/statement")
    public ResponseEntity<Account> accountStatement(@Valid @PathVariable("id") Long accountId) {

        log.info("Account statement requested for account id: {}", accountId);
        try {
            Account account = accountService.getAccountDetails(accountId);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch(AccountNotFoundException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    /**
     * This api is to transfer money between accounts.
     * @param fromAccountId source account
     * @param toAccountId   destination account
     * @param amount        amount to be transferred
     * @return              return 201 for successful transfer
     */
    @PostMapping("/account/{from}/transfer/{to}/{amount}")
    public ResponseEntity<AccountTransferPair> transferMoney(@Valid @PathVariable(value = "from") Long fromAccountId, @Valid @PathVariable("to") Long toAccountId, @Valid @PathVariable("amount") BigDecimal amount) {

        log.info("Money transfer requested from account id: '{}' to account id: '{}' for amount: '{}'.", fromAccountId, toAccountId, amount);
        if(fromAccountId.equals(toAccountId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both from and to account ids are same");
        try {
            AccountTransferPair result = accountService.transfer(fromAccountId, toAccountId, amount);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch(AccountNotFoundException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        } catch(InsufficientBalanceException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
