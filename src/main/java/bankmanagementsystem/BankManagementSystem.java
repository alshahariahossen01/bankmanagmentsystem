package octeto.bankmanagementsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * BankManagementSystem.java
 *
 * A simple Bank Management System demonstrating:
 * - Encapsulation (private fields + public methods)
 * - Inheritance (Account -> SavingsAccount, CurrentAccount)
 * - Polymorphism (Bank works with Account references)
 * - Method overriding (CurrentAccount.withdraw allows overdraft)
 * - Abstraction (Account methods provide a simple interface)
 */

/* ---------- Base Account Class (abstract) ---------- */
abstract class Account {
    private final String accountNumber;
    private final String accountHolder;
    protected double balance;

    public Account(String accountNumber, String accountHolder, double initialBalance) {
        if (accountNumber == null || accountHolder == null) {
            throw new IllegalArgumentException("Account number and holder cannot be null.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive.");
        balance += amount;
        System.out.printf("Deposited %.2f to %s (New balance: %.2f)%n", amount, accountNumber, balance);
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive.");
        if (amount > balance) {
            System.out.printf("Withdrawal of %.2f from %s failed: insufficient funds (balance: %.2f)%n",
                    amount, accountNumber, balance);
            return false;
        }
        balance -= amount;
        System.out.printf("Withdrew %.2f from %s (New balance: %.2f)%n", amount, accountNumber, balance);
        return true;
    }

    public void checkBalance() {
        System.out.printf("Account %s (Holder: %s) — Balance: %.2f%n", accountNumber, accountHolder, balance);
    }

    public abstract String getAccountType();

    @Override
    public String toString() {
        return String.format("%s Account[%s] Holder: %s Balance: %.2f",
                getAccountType(), accountNumber, accountHolder, balance);
    }
}

/* ---------- SavingsAccount Subclass ---------- */
class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accountNumber, String accountHolder, double initialBalance, double interestRate) {
        super(accountNumber, accountHolder, initialBalance);
        setInterestRate(interestRate);
    }

    public double getInterestRate() {
        return interestRate;
    }

    public final void setInterestRate(double interestRate) {
        if (interestRate < 0) throw new IllegalArgumentException("Interest rate cannot be negative.");
        this.interestRate = interestRate;
    }

    public void addInterest() {
        double interest = balance * interestRate;
        if (interest > 0) {
            deposit(interest);
            System.out.printf("Interest %.2f added to Savings %s at rate %.2f%%%n",
                    interest, getAccountNumber(), interestRate * 100);
        } else {
            System.out.printf("No interest added to %s%n", getAccountNumber());
        }
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }
}

/* ---------- CurrentAccount Subclass ---------- */
class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount(String accountNumber, String accountHolder, double initialBalance, double overdraftLimit) {
        super(accountNumber, accountHolder, initialBalance);
        setOverdraftLimit(overdraftLimit);
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public final void setOverdraftLimit(double overdraftLimit) {
        if (overdraftLimit < 0) throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive.");
        double allowed = balance + overdraftLimit;
        if (amount > allowed) {
            System.out.printf("Withdrawal of %.2f from %s failed: exceeds overdraft limit (allowed: %.2f)%n",
                    amount, getAccountNumber(), allowed);
            return false;
        }
        balance -= amount;
        System.out.printf("Withdrew %.2f from %s (New balance: %.2f) [Overdraft limit: %.2f]%n",
                amount, getAccountNumber(), balance, overdraftLimit);
        return true;
    }

    @Override
    public String getAccountType() {
        return "Current";
    }
}

/* ---------- Bank Class ---------- */
class Bank {
    private final List<Account> accounts = new ArrayList<>();

    public void addAccount(Account account) {
        if (account == null) throw new IllegalArgumentException("Account cannot be null.");
        if (findAccountByNumber(account.getAccountNumber()).isPresent()) {
            throw new IllegalArgumentException("Account with number " + account.getAccountNumber() + " already exists.");
        }
        accounts.add(account);
        System.out.println("Added " + account);
    }

    public Optional<Account> findAccountByNumber(String accountNumber) {
        return accounts.stream().filter(a -> a.getAccountNumber().equals(accountNumber)).findFirst();
    }

    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Transfer amount must be positive.");
        Optional<Account> fromOpt = findAccountByNumber(fromAccountNumber);
        Optional<Account> toOpt = findAccountByNumber(toAccountNumber);

        if (!fromOpt.isPresent()) {
            System.out.println("From-account not found: " + fromAccountNumber);
            return false;
        }
        if (!toOpt.isPresent()) {
            System.out.println("To-account not found: " + toAccountNumber);
            return false;
        }

        Account from = fromOpt.get();
        Account to = toOpt.get();

        if (!from.withdraw(amount)) {
            System.out.println("Transfer aborted: insufficient funds or limit reached.");
            return false;
        }

        to.deposit(amount);
        System.out.printf("Transfer of %.2f from %s to %s completed.%n",
                amount, fromAccountNumber, toAccountNumber);
        return true;
    }

    public void printAllAccounts() {
        System.out.println("=== Bank Accounts Summary ===");
        for (Account a : accounts) {
            System.out.println(a);
        }
    }
}

/* ---------- Main Class ---------- */
public class BankManagementSystem {
    public static void main(String[] args) {
        Bank myBank = new Bank();

        SavingsAccount s1 = new SavingsAccount("SAV1001", "Alice Johnson", 1500.00, 0.03);
        CurrentAccount c1 = new CurrentAccount("CUR2001", "Bob Smith", 500.00, 300.00);

        myBank.addAccount(s1);
        myBank.addAccount(c1);

        s1.checkBalance();
        c1.checkBalance();

        s1.deposit(200.00);
        c1.withdraw(600.00);
        c1.withdraw(1000.00); // Should fail

        s1.addInterest();

        myBank.transfer("SAV1001", "CUR2001", 300.00);

        myBank.printAllAccounts();

        Account poly = new SavingsAccount("SAV3002", "Charlie Park", 800.00, 0.05);
        myBank.addAccount(poly);
        poly.checkBalance();
        if (poly instanceof SavingsAccount) {
            ((SavingsAccount) poly).addInterest();
        }

        myBank.printAllAccounts();
    }
}
