package service.bank;

import java.math.BigDecimal;
import java.util.Set;

public class Account {
    private static final int MAX_SCALE = 2;
    private String accountNumber;
    private BigDecimal balance;
    private Set<Client> holders;

    public Account(String accountNumber, BigDecimal balance, Client client) {
        this(accountNumber, balance, Set.of(client));
    }

    public Account(String accountNumber, BigDecimal balance, Set<Client> holders) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.holders = holders;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Set<Client> getHolders() {
        return holders;
    }

    public void deposit(BigDecimal amount) {
        if (amount.scale() > MAX_SCALE) {
            throw new IllegalArgumentException("The scale can't be greater than " + MAX_SCALE);
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.scale() > MAX_SCALE) {
            throw new IllegalArgumentException("The scale can't be greater than " + MAX_SCALE);
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        this.balance = this.balance.subtract(amount);
    }

}
