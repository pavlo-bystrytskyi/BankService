package service.bank;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class AccountTest {

    @Test
    void depositTest_someAmount() {
        Client client = new Client("John", "Doe", 1);
        Account account = new Account("abc123", new BigDecimal("100.51"), client);
        BigDecimal amountToAdd = new BigDecimal("99.49");

        BigDecimal expectedBalance = new BigDecimal("200");
        account.deposit(amountToAdd);
        BigDecimal actualBalance = account.getBalance();

        assertEquals(0, expectedBalance.compareTo(actualBalance));
    }

    @Test
    void depositTest_zeroAmount() {
        Client client = new Client("John", "Doe", 1);
        Account account = new Account("abc123", new BigDecimal("100.51"), client);
        BigDecimal amountToAdd = new BigDecimal("0");

        BigDecimal expectedBalance = new BigDecimal("100.51");
        account.deposit(amountToAdd);
        BigDecimal actualBalance = account.getBalance();

        assertEquals(0, expectedBalance.compareTo(actualBalance));

    }

    @Test
    void depositTest_negativeAmount() {
        Client client = new Client("John", "Doe", 1);
        Account account = new Account("abc123", new BigDecimal("100.51"), client);
        BigDecimal amountToAdd = new BigDecimal("-99.49");

        assertThrowsExactly(IllegalArgumentException.class, () -> account.deposit(amountToAdd));
    }

    @Test
    void depositTest_incorrectAmount() {
        Client client = new Client("John", "Doe", 1);
        Account account = new Account("abc123", new BigDecimal("100.51"), client);
        BigDecimal amountToAdd = new BigDecimal("99.490");

        assertThrowsExactly(IllegalArgumentException.class, () -> account.deposit(amountToAdd));
    }

    @Test
    void withdrawTest_someAmount() {
        Client client = new Client("John", "Doe", 1);
        Account account = new Account("abc123", new BigDecimal("100.51"), client);
        BigDecimal amountToWithdraw = new BigDecimal("99.49");

        BigDecimal expectedBalance = new BigDecimal("1.02");
        account.withdraw(amountToWithdraw);
        BigDecimal actualBalance = account.getBalance();

        assertEquals(0, expectedBalance.compareTo(actualBalance));
    }

    @Test
    void withdrawTest_zeroAmount() {
        Client client = new Client("John", "Doe", 1);
        Account account = new Account("abc123", new BigDecimal("100.51"), client);
        BigDecimal amountToWithdraw = new BigDecimal(BigDecimal.ZERO.toString());

        BigDecimal expectedBalance = new BigDecimal("100.51");
        account.withdraw(amountToWithdraw);
        BigDecimal actualBalance = account.getBalance();

        assertEquals(0, expectedBalance.compareTo(actualBalance));
    }

    @Test
    void withdrawTest_allAmount() {
        Client client = new Client("John", "Doe", 1);
        Account account = new Account("abc123", new BigDecimal("100.51"), client);
        BigDecimal amountToWithdraw = new BigDecimal("100.51");

        BigDecimal expectedBalance = new BigDecimal(BigDecimal.ZERO.toString());
        account.withdraw(amountToWithdraw);
        BigDecimal actualBalance = account.getBalance();

        assertEquals(0, expectedBalance.compareTo(actualBalance));
    }

    @Test
    void withdrawTest_negativeAmount() {
        Client client = new Client("John", "Doe", 1);
        Account account = new Account("abc123", new BigDecimal("100.51"), client);
        BigDecimal amountToWithdraw = new BigDecimal("-99.49");

        assertThrowsExactly(IllegalArgumentException.class, () -> account.withdraw(amountToWithdraw));
    }

    @Test
    void withdrawTest_incorrectAmount() {
        Client client = new Client("John", "Doe", 1);
        Account account = new Account("abc123", new BigDecimal("100.51"), client);
        BigDecimal amountToWithdraw = new BigDecimal("99.490");

        assertThrowsExactly(IllegalArgumentException.class, () -> account.withdraw(amountToWithdraw));
    }

    @Test
    void withdrawTest_tooBigAmount() {
        Client client = new Client("John", "Doe", 1);
        Account account = new Account("abc123", new BigDecimal("100.51"), client);
        BigDecimal amountToWithdraw = new BigDecimal("100.52");

        assertThrowsExactly(IllegalArgumentException.class, () -> account.withdraw(amountToWithdraw));
    }
}