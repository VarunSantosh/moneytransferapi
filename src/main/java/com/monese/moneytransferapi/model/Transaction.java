package com.monese.moneytransferapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "transaction")
@IdClass(TransactionPK.class)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 6435157498901133988L;

    @Id
    @Column(name = "transaction_id", nullable = false)
    @JsonProperty("transaction_id")
    private Long transactionId;


    @Column(name = "transaction_amount", nullable = false)
    @JsonProperty("transaction_amount")
    private BigDecimal transactionAmount;

    @Id
    @Column(name = "transaction_type", nullable = false)
    @JsonProperty("transaction_type")
    private String transactionType;

    @Column(name = "transaction_date", nullable = true)
    @JsonProperty("transaction_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private Account account;

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}