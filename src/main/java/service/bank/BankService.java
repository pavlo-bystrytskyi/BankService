package service.bank;

import java.math.BigDecimal;
import java.util.HashMap;

public class BankService {
    private HashMap<String, Account> accounts = new HashMap<>();

    public String openAccount(Client client) {
        Account account = new Account("", new BigDecimal(0), client);
        accounts.put(account.getAccountNumber(), account);

        return account.getAccountNumber();
    }

    public void transferBalance(String senderAccountNumber, String recipientAccountNumber, BigDecimal amount) {
        if (senderAccountNumber.equals(recipientAccountNumber)) {
            throw new IllegalArgumentException("Sender account number and recipient account number should not match");
        }
        Account senderAccount = accounts.get(senderAccountNumber);
        if (senderAccount == null) {
            throw new IllegalArgumentException("Sender account not found");
        }
        Account recipientAccount = accounts.get(recipientAccountNumber);
        if (recipientAccount == null) {
            throw new IllegalArgumentException("Recipient account not found");
        }

        try {
            senderAccount.withdraw(amount);
        } catch (Exception e) {
            throw new IllegalArgumentException("Transfer failed", e);
        }

        try {
            recipientAccount.deposit(amount);
        } catch (Exception e) {
            senderAccount.deposit(amount);
            throw new IllegalArgumentException("Transfer failed", e);
        }
    }
}
