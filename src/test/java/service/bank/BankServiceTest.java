package service.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BankServiceTest {

    @Test
    void openAccountTest() {
        BankService bankService = new BankService();
        Client john = new Client("John", "Doe", 1);
        Client jane = new Client("Jane", "Doe", 2);
        String accountNumberJohn = bankService.openAccount(john);
        String accountNumberJane = bankService.openAccount(jane);

        Assertions.assertNotNull(accountNumberJohn);
        Assertions.assertNotNull(accountNumberJane);
        assertNotEquals(accountNumberJane, accountNumberJohn);
    }

    @Test
    void getAccountTest_existingAccount() {
        BankService bankService = new BankService();
        Client john = new Client("John", "Doe", 1);
        Client jane = new Client("Jane", "Doe", 2);
        String accountNumberJohn = bankService.openAccount(john);
        bankService.openAccount(jane);
        Account accountJohn = bankService.getAccount(accountNumberJohn);

        Client actualClient = accountJohn.getHolders().stream().findFirst().get();

        assertEquals(john, actualClient);
    }

    @Test
    void getAccountTest_notExistingAccount() {
        BankService bankService = new BankService();
        Client john = new Client("John", "Doe", 1);
        Client jane = new Client("Jane", "Doe", 2);
        bankService.openAccount(john);
        bankService.openAccount(jane);

        Account actualAccount = bankService.getAccount("some_account_number");

        assertNull(actualAccount);
    }

    @Test
    void transferBalanceTest_someAmount() {
        BankService bankService = new BankService();
        Client john = new Client("John", "Doe", 1);
        Client jane = new Client("Jane", "Doe", 2);
        String accountNumberJohn = bankService.openAccount(john);
        Account accountJohn = bankService.getAccount(accountNumberJohn);
        String accountNumberJane = bankService.openAccount(jane);
        Account accountJane = bankService.getAccount(accountNumberJane);
        accountJohn.deposit(new BigDecimal("1000.22"));
        accountJane.deposit(new BigDecimal("1000.23"));
        bankService.transferBalance(accountNumberJohn, accountNumberJane, new BigDecimal("155.55"));

        BigDecimal expectedJohnBalance = new BigDecimal("844.67");
        BigDecimal expectedJaneBalance = new BigDecimal("1155.78");
        BigDecimal actualJohnBalance = accountJohn.getBalance();
        BigDecimal actualJaneBalance = accountJane.getBalance();

        assertEquals(0, expectedJohnBalance.compareTo(actualJohnBalance));
        assertEquals(0, expectedJaneBalance.compareTo(actualJaneBalance));
    }

    @Test
    void transferBalanceTest_allAmount() {
        BankService bankService = new BankService();
        Client john = new Client("John", "Doe", 1);
        Client jane = new Client("Jane", "Doe", 2);
        String accountNumberJohn = bankService.openAccount(john);
        Account accountJohn = bankService.getAccount(accountNumberJohn);
        String accountNumberJane = bankService.openAccount(jane);
        Account accountJane = bankService.getAccount(accountNumberJane);
        accountJohn.deposit(new BigDecimal("1000.22"));
        accountJane.deposit(new BigDecimal("1000.23"));
        bankService.transferBalance(accountNumberJohn, accountNumberJane, new BigDecimal("1000.22"));

        BigDecimal expectedJohnBalance = new BigDecimal("0");
        BigDecimal expectedJaneBalance = new BigDecimal("2000.45");
        BigDecimal actualJohnBalance = accountJohn.getBalance();
        BigDecimal actualJaneBalance = accountJane.getBalance();

        assertEquals(0, expectedJohnBalance.compareTo(actualJohnBalance));
        assertEquals(0, expectedJaneBalance.compareTo(actualJaneBalance));
    }

    @Test
    void transferBalanceTest_negativeAmount() {
        BankService bankService = new BankService();
        Client john = new Client("John", "Doe", 1);
        Client jane = new Client("Jane", "Doe", 2);
        String accountNumberJohn = bankService.openAccount(john);
        Account accountJohn = bankService.getAccount(accountNumberJohn);
        String accountNumberJane = bankService.openAccount(jane);
        Account accountJane = bankService.getAccount(accountNumberJane);
        accountJohn.deposit(new BigDecimal("1000.22"));
        accountJane.deposit(new BigDecimal("1000.23"));
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> bankService.transferBalance(accountNumberJohn, accountNumberJane, new BigDecimal("-1000.22"))
        );
    }

    @Test
    void transferBalanceTest_tooBigAmount() {
        BankService bankService = new BankService();
        Client john = new Client("John", "Doe", 1);
        Client jane = new Client("Jane", "Doe", 2);
        String accountNumberJohn = bankService.openAccount(john);
        Account accountJohn = bankService.getAccount(accountNumberJohn);
        String accountNumberJane = bankService.openAccount(jane);
        Account accountJane = bankService.getAccount(accountNumberJane);
        accountJohn.deposit(new BigDecimal("1000.22"));
        accountJane.deposit(new BigDecimal("1000.23"));

        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> bankService.transferBalance(accountNumberJohn, accountNumberJane, new BigDecimal("1000.23"))
        );
    }

    @Test
    void transferBalanceTest_zeroAmount() {
        BankService bankService = new BankService();
        Client john = new Client("John", "Doe", 1);
        Client jane = new Client("Jane", "Doe", 2);
        String accountNumberJohn = bankService.openAccount(john);
        Account accountJohn = bankService.getAccount(accountNumberJohn);
        String accountNumberJane = bankService.openAccount(jane);
        Account accountJane = bankService.getAccount(accountNumberJane);
        accountJohn.deposit(new BigDecimal("1000.22"));
        accountJane.deposit(new BigDecimal("1000.23"));
        bankService.transferBalance(accountNumberJohn, accountNumberJane, new BigDecimal("0"));

        BigDecimal expectedJohnBalance = new BigDecimal("1000.22");
        BigDecimal expectedJaneBalance = new BigDecimal("1000.23");
        BigDecimal actualJohnBalance = accountJohn.getBalance();
        BigDecimal actualJaneBalance = accountJane.getBalance();

        assertEquals(0, expectedJohnBalance.compareTo(actualJohnBalance));
        assertEquals(0, expectedJaneBalance.compareTo(actualJaneBalance));
    }

    @Test
    void splitTest_someAmount() {
        BankService bankService = new BankService();
        Client john = new Client("John", "Doe", 1);
        Client jane = new Client("Jane", "Doe", 2);
        Client alice = new Client("Alice", "Doe", 3);
        String accountNumber = bankService.openAccount(Set.of(john, jane, alice));
        Account account = bankService.getAccount(accountNumber);
        account.deposit(new BigDecimal("1000.00"));
        List<String> newAccounts = bankService.split(accountNumber);

        BigDecimal expectedFirstBalance = new BigDecimal("333.33");
        BigDecimal expectedSecondBalance = new BigDecimal("333.33");
        BigDecimal expectedThirdBalance = new BigDecimal("333.34");
        BigDecimal actualFirstBalance = bankService.getAccount(newAccounts.get(0)).getBalance();
        BigDecimal actualSecondBalance = bankService.getAccount(newAccounts.get(1)).getBalance();
        BigDecimal actualThirdBalance = bankService.getAccount(newAccounts.get(2)).getBalance();

        assertEquals(0, expectedFirstBalance.compareTo(actualFirstBalance));
        assertEquals(0, expectedSecondBalance.compareTo(actualSecondBalance));
        assertEquals(0, expectedThirdBalance.compareTo(actualThirdBalance));
    }
}