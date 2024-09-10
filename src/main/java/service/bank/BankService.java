package service.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<String> split(String accountNumber) {
        Account originalAccount = accounts.get(accountNumber);
        if (originalAccount == null) {
            throw new IllegalArgumentException("Account not found");
        }
        int accountHoldersNumber = originalAccount.getHolders().size();
        if (accountHoldersNumber == 0) {
            throw new IllegalArgumentException("Account holders not found");
        }
        BigDecimal averageBalance = originalAccount.getBalance().divide(new BigDecimal(accountHoldersNumber), RoundingMode.HALF_DOWN);
        List<Account> newAccounts = new ArrayList<>();
        originalAccount.getHolders().forEach(
                holder -> {
                    String newAccountNumber = this.openAccount(holder);
                    Account newAccount = this.getAccount(newAccountNumber);
                    newAccount.deposit(averageBalance);
                    newAccounts.add(newAccount);
                });
        BigDecimal totalBalance = newAccounts.stream().map(Account::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal difference = totalBalance.subtract(originalAccount.getBalance());
        if (totalBalance.compareTo(originalAccount.getBalance()) < 0) {
            newAccounts.getLast().deposit(difference.negate());
        }

        if (totalBalance.compareTo(originalAccount.getBalance()) > 0) {
            newAccounts.getLast().withdraw(difference);
        }
        closeAccount(originalAccount.getAccountNumber());

        return newAccounts.stream().map(Account::getAccountNumber).collect(Collectors.toList());
    }

    private void closeAccount(String accountNumber) {
        this.accounts.remove(accountNumber);
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
