package com.monese.moneytransferapi.exception;

public class InsufficientBalanceException  extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}