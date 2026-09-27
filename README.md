# Banking Transaction Analysis and Anomaly Detection

A Java 17 banking application for managing **clients, bank accounts, and transactions** using **JDBC** and **MySQL/MariaDB**.

The application provides a layered architecture and will support financial analysis, suspicious transaction detection, and reporting.

## 📌 Project Overview

This project was developed as an individual Java project to practice:

* Java 17
* Object-Oriented Programming
* Records
* Sealed classes
* Enums
* JDBC
* MySQL/MariaDB
* DAO pattern
* Service layer
* Java Streams
* Collectors
* Optional
* Exception handling
* Git

The application allows a bank employee or analyst to manage banking data and analyze transaction activity.

---

## 🛠️ Technologies

| Technology      | Usage                   |
| --------------- | ----------------------- |
| Java 17         | Application development |
| JDBC            | Database connectivity   |
| MySQL / MariaDB | Database                |
| Git             | Version control         |
| Maven           | Not required            |
| Linux           | Development environment |

---

## 🏗️ Architecture

The project follows a simple layered architecture:

```text
┌──────────────────────────┐
│           UI             │
│      Console / Menu      │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│         Service          │
│    Business Logic        │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│           DAO            │
│   Database Operations    │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│       MySQL/MariaDB      │
│     banking_analysis     │
└──────────────────────────┘
```

### Project Structure

```text
Banking-Transaction/
│
├── src/
│   ├── model/
│   │   ├── Client.java
│   │   ├── Account.java
│   │   ├── CurrentAccount.java
│   │   ├── SavingsAccount.java
│   │   ├── Transaction.java
│   │   └── TransactionType.java
│   │
│   ├── DAO/
│   │   ├── ClientDAO.java
│   │   ├── AccountDAO.java
│   │   └── TransactionDAO.java
│   │
│   ├── service/
│   │   ├── ClientService.java
│   │   ├── AccountService.java
│   │   └── TransactionService.java
│   │
│   ├── ui/
│   │   └── Menu.java
│   │
│   ├── utils/
│   │   └── DatabaseConnection.java
│   │
│   └── Main.java
│
├── database/
│   └── schema.sql
│
├── README.md
└── .gitignore
```

---

# 🗄️ Database

The application uses a database called:

```text
banking_analysis
```

The database contains three main tables:

```text
clients
   │
   │ 1
   │
   │ *
accounts
   │
   │ 1
   │
   │ *
transactions
```

### Clients

Stores customer information:

```text
id
name
email
```

### Accounts

Stores bank accounts:

```text
id
number
balance
client_id
account_type
authorized_overdraft
interest_rate
```

Two account types are supported:

* `CURRENT`
* `SAVINGS`

### Transactions

Stores banking transactions:

```text
id
transaction_date
amount
transaction_type
location
account_id
```

Supported transaction types:

```text
DEPOSIT
WITHDRAWAL
TRANSFER
```

---

# ☕ Java Model

## Client

`Client` is implemented as a Java record:

```java
public record Client(
    Long id,
    String name,
    String email
) {}
```

## Account

`Account` is a sealed abstract class:

```java
public sealed abstract class Account
        permits CurrentAccount, SavingsAccount
```

This allows only the defined account types to extend `Account`.

### CurrentAccount

Contains:

```text
authorizedOverdraft
```

The authorized overdraft represents the maximum amount by which the account can go below zero.

### SavingsAccount

Contains:

```text
interestRate
```

## Transaction

`Transaction` is implemented as a Java record:

```java
public record Transaction(
    Long id,
    LocalDateTime date,
    double amount,
    TransactionType type,
    String location,
    Long accountId
) {}
```

---

# 🔌 JDBC

The application uses JDBC to communicate with MySQL/MariaDB.

Database connection:

```text
jdbc:mysql://localhost:3306/banking_analysis
```

The connection is centralized in:

```text
src/utils/DatabaseConnection.java
```

---

# 📦 DAO Layer

The DAO layer is responsible for database operations.

### ClientDAO

Supports:

* Create client
* Find client by ID
* List clients
* Update client
* Delete client

### AccountDAO

Supports:

* Create current account
* Create savings account
* Find account by ID
* List accounts
* Find accounts by client
* Update account
* Delete account

### TransactionDAO

Supports:

* Create transaction
* Find transaction by ID
* List transactions
* Find transactions by account
* Update transaction
* Delete transaction

---

# ⚙️ Service Layer

The service layer contains the application's business logic.

### ClientService

Handles client-related operations and validation.

### AccountService

Handles account operations such as:

* Account creation
* Account updates
* Account search
* Balance-related operations

### TransactionService

Handles transaction operations and analysis.

Current analysis features include:

* Filtering transactions by type
* Calculating total transaction amounts
* Calculating totals by transaction type
* Grouping transactions by type
* Detecting suspicious transactions based on defined rules

Java Streams and Collectors are used for transaction analysis.

Example:

```java
transactions.stream()
    .filter(transaction -> transaction.type() == TransactionType.DEPOSIT)
    .mapToDouble(Transaction::amount)
    .sum();
```

---

# 🖥️ Console Application

The application provides an interactive console menu.

```text
==============================
     BANKING ANALYSIS
==============================

1. Client management
2. Account management
3. Transaction management
0. Exit
```

### Client Management

```text
1. Create client
2. Find client by ID
3. List all clients
4. Update client
5. Delete client
0. Back
```

### Account Management

```text
1. Create current account
2. Create savings account
3. Find account by ID
4. List all accounts
5. List accounts by client
6. Update account
7. Delete account
0. Back
```

### Transaction Management

```text
1. Create transaction
2. Find transaction by ID
3. List all transactions
4. List transactions by account
5. Filter transactions by type
6. Calculate total amount
7. Calculate total by type
8. Show suspicious transactions
9. Update transaction
10. Delete transaction
0. Back
```

---

# 🚀 Installation

## 1. Clone the repository

```bash
git clone <YOUR_REPOSITORY_URL>
cd Banking-Transaction
```

## 2. Create the database

Open MariaDB/MySQL:

```bash
mariadb -u root -p
```

Then execute:

```sql
source database/schema.sql;
```

Or:

```bash
mariadb -u root -p < database/schema.sql
```

---

# 🔧 Database Configuration

Open:

```text
src/utils/DatabaseConnection.java
```

Configure:

```java
private static final String URL =
        "jdbc:mysql://localhost:3306/banking_analysis";

private static final String USER = "root";

private static final String PASSWORD =
        "YOUR_PASSWORD";
```

Replace `YOUR_PASSWORD` with your database password.

---

# 📚 JDBC Driver

The project requires the MySQL Connector/J driver.

On the development environment, the driver can be located at:

```text
/usr/share/java/mysql-connector-j.jar
```

---

# ▶️ Compile

From the project root:

```bash
rm -rf out
mkdir out

javac \
    -cp "/usr/share/java/mysql-connector-j.jar" \
    -d out \
    $(find src -name "*.java")
```

---

# ▶️ Run

```bash
java \
    -cp "out:/usr/share/java/mysql-connector-j.jar" \
    Main
```

---

# 🔍 Example

Creating a current account:

```text
---------- ACCOUNTS ----------

1. Create current account
2. Create savings account
3. Find account by ID
...

Choose: 1

Account number: 1001
Initial balance: 5000
Client ID: 2
Authorized overdraft: 500

Account created successfully.
```

Creating a transaction:

```text
---------- TRANSACTIONS ----------

1. Create transaction
2. Find transaction by ID
...

Choose: 1

Amount: 1500

Transaction type:
1. DEPOSIT
2. WITHDRAWAL
3. TRANSFER

Choose: 2

Location: Casablanca

Account ID: 1

Transaction created.
```

---

# 📊 Planned Analysis

The project is designed to provide additional financial analysis such as:

* Top clients by total balance
* Monthly transaction reports
* Suspicious transaction detection
* Inactive account detection
* Transaction statistics
* Transaction grouping and aggregation
* Financial reports

---

# 🔐 Data Integrity

The database uses:

* Primary keys
* Foreign keys
* Unique constraints
* `NOT NULL` constraints
* Enumerated account and transaction types
* Referential integrity

Relationships:

```text
Client 1 ─────── * Account

Account 1 ─────── * Transaction
```

---

# 🧪 Testing

The application can be tested directly through the console menu.

Recommended test order:

```text
1. Create a client
2. Create a current account
3. Create a savings account
4. List accounts
5. Create transactions
6. List transactions
7. Filter transactions
8. Calculate totals
9. Test suspicious transactions
10. Update/delete records
```

---

# 📈 Future Improvements

Possible improvements include:

* CSV report export
* JSON report export
* File logging
* More advanced anomaly detection
* Monthly financial reports
* Inactive account detection
* Better transaction validation
* Automated unit tests
* Executable JAR
* Improved console interface

---

# 👨‍💻 Author

**Aymane El Khadraoui**

Java / Full Stack Developer

---

# 📄 License

This project was created for educational purposes.

