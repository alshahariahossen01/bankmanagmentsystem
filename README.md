# 💳 Bank Management System (Java)

This is a **console-based Bank Management System** built in Java that showcases fundamental **Object-Oriented Programming (OOP)** concepts:

- **Encapsulation** — via private fields and public accessors
- **Abstraction** — through abstract class `Account`
- **Inheritance** — with `SavingsAccount` and `CurrentAccount` extending `Account`
- **Polymorphism** — Bank works with `Account` references to both account types
- **Method Overriding** — `CurrentAccount` overrides `withdraw` to allow overdraft

---

## 📦 Features

- Create and manage **Savings** and **Current** accounts
- Perform **deposits** and **withdrawals**
- Apply **interest** on savings accounts
- Allow **overdrafts** for current accounts
- Transfer funds between accounts
- Polymorphic behavior with abstract class usage
- Console output for real-time feedback

---

## 🛠️ Technologies

- Java 8+
- No external libraries (pure Java)

---

## 📁 Project Structure


If you want to refactor into multiple files:
- `Account.java` (abstract)
- `SavingsAccount.java`
- `CurrentAccount.java`
- `Bank.java`
- `BankManagementSystem.java`

---

## 🚀 How to Run

1. Clone the repo:
   ```bash
   git clone https://github.com/alshahariahossen01/bank-management-system-java.git
   cd bank-management-system-java
Compile and run:

javac octeto/bankmanagementsystem/BankManagementSystem.java
java octeto.bankmanagementsystem.BankManagementSystem


Added Savings Account[SAV1001] Holder: Alice Johnson Balance: 1500.00
Added Current Account[CUR2001] Holder: Bob Smith Balance: 500.00
Deposited 200.00 to SAV1001 (New balance: 1700.00)
Withdrew 600.00 from CUR2001 (New balance: -100.00) [Overdraft limit: 300.00]
...

