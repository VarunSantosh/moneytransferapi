package com.monese.moneytransferapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountTransferPair {

    @JsonProperty("transaction_id")
    private Long id;

    @JsonProperty("from_account")
    private Long fromAccount;

    @JsonProperty("to_account")
    private Long toAccount;

    @JsonProperty("amount")
    private BigDecimal amount;

    public AccountTransferPair(Long id, Long fromAccount, Long toAccount, BigDecimal amount) {
        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Long getFromAccount() {
        return fromAccount;
    }

    public Long getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
