package service.bank;

import java.math.BigDecimal;

public class Account {
    private String accountNumber;
    private BigDecimal balance;
    private Client client;

    public Account(String accountNumber, BigDecimal balance, Client client) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.client = client;
    }
}
