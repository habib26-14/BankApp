package org.emsi.ebankingbackend.exceptions;

public class BalanceNotsufficientException extends Exception {
    public BalanceNotsufficientException(String message) {
        super(message);
    }
}
