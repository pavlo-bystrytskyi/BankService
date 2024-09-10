package service.bank;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;

public class BankService {
    private static int accountCounter = 0;
    private HashMap<String, Account> accounts = new HashMap<>();

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public String openAccount(Client client) {
        return this.openAccount(Set.of(client));
    }

    public String openAccount(Set<Client> holders) {
        Account account = new Account(this.generateAccountNumber(), new BigDecimal(0), holders);
        accounts.put(account.getAccountNumber(), account);

        return account.getAccountNumber();
    }

    private String generateAccountNumber() {
        return String.format("%016d", ++accountCounter);
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
