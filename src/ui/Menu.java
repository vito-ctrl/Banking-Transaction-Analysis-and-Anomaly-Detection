package ui;

import dao.AccountDAO;
import dao.ClientDAO;
import model.Account;
import model.Client;
import model.CurrentAccount;
import model.SavingsAccount;

import model.Transaction;
import model.TransactionType;
import service.TransactionService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.time.LocalDateTime;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);

    private final ClientDAO clientDAO = new ClientDAO();
    private final AccountDAO accountDAO = new AccountDAO();

    private final TransactionService transactionService;

    public Menu() {
        // this.clientService = new ClientService();
        // this.accountService = new AccountService();
        this.transactionService = new TransactionService();
    }

    public void start() {

        while (true) {

            System.out.println("\n==============================");
            System.out.println("     BANKING ANALYSIS");
            System.out.println("==============================");
            System.out.println("1. Client management");
            System.out.println("2. Account management");
            System.out.println("3. Transaction management");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> clientMenu();

                case 2 -> accountMenu();

                case 3 -> transactionMenu();
                
                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }

                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void clientMenu() {

        while (true) {

            System.out.println("\n---------- CLIENTS ----------");
            System.out.println("1. Create client");
            System.out.println("2. Find client by ID");
            System.out.println("3. List all clients");
            System.out.println("4. Update client");
            System.out.println("5. Delete client");
            System.out.println("0. Back");

            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (choice) {

                    case 1 -> createClient();

                    case 2 -> findClient();

                    case 3 -> listClients();

                    case 4 -> updateClient();

                    case 5 -> deleteClient();

                    case 0 -> {
                        return;
                    }

                    default -> System.out.println("Invalid choice!");
                }

            } catch (Exception e) {

                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void transactionMenu() {
        while (true) {

            System.out.println();
            System.out.println("---------- TRANSACTIONS ----------");
            System.out.println("1. Create transaction");
            System.out.println("2. Find transaction by ID");
            System.out.println("3. List all transactions");
            System.out.println("4. List transactions by account");
            System.out.println("5. Filter transactions by type");
            System.out.println("6. Calculate total amount");
            System.out.println("7. Calculate total by type");
            System.out.println("8. Show suspicious transactions");
            System.out.println("9. Update transaction");
            System.out.println("10. Delete transaction");
            System.out.println("0. Back");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (choice) {

                    case 1 -> createTransaction();

                    case 2 -> findTransaction();

                    case 3 -> listTransactions();

                    case 4 -> listTransactionsByAccount();

                    case 5 -> filterTransactionsByType();

                    case 6 -> calculateTotal();

                    case 7 -> calculateTotalByType();

                    case 8 -> showSuspiciousTransactions();

                    case 9 -> updateTransaction();

                    case 10 -> deleteTransaction();

                    case 0 -> {
                        return;
                    }

                    default ->
                            System.out.println("Invalid choice.");

                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void createTransaction() throws Exception {

        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Transaction type:");
        System.out.println("1. DEPOSIT");
        System.out.println("2. WITHDRAWAL");
        System.out.println("3. TRANSFER");

        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        TransactionType type = switch (typeChoice) {
            case 1 -> TransactionType.DEPOSIT;
            case 2 -> TransactionType.WITHDRAWAL;
            case 3 -> TransactionType.TRANSFER;
            default -> throw new IllegalArgumentException("Invalid type.");
        };

        System.out.print("Location: ");
        String location = scanner.nextLine();

        System.out.print("Account ID: ");
        Long accountId = scanner.nextLong();
        scanner.nextLine();

        Transaction transaction = new Transaction(
                null,
                LocalDateTime.now(),
                amount,
                type,
                location,
                accountId
        );

        Transaction created =
                transactionService.create(transaction);

        System.out.println("Transaction created:");
        System.out.println(created);
    }

    private void findTransaction() throws Exception {

        System.out.print("Transaction ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        transactionService.findById(id)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Transaction not found.")
                );
    }

    private void listTransactions() throws Exception {

        List<Transaction> transactions =
                transactionService.findAll();

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        transactions.forEach(System.out::println);
    }

    private void listTransactionsByAccount() throws Exception {

        System.out.print("Account ID: ");
        Long accountId = scanner.nextLong();
        scanner.nextLine();

        List<Transaction> transactions =
                transactionService.findByAccountId(accountId);

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        transactions.forEach(System.out::println);
    }

    private void filterTransactionsByType() throws Exception {

        System.out.println("1. DEPOSIT");
        System.out.println("2. WITHDRAWAL");
        System.out.println("3. TRANSFER");

        int choice = scanner.nextInt();
        scanner.nextLine();

        TransactionType type = switch (choice) {
            case 1 -> TransactionType.DEPOSIT;
            case 2 -> TransactionType.WITHDRAWAL;
            case 3 -> TransactionType.TRANSFER;
            default -> throw new IllegalArgumentException("Invalid type.");
        };

        transactionService.findByType(type)
                .forEach(System.out::println);
    }

    private void calculateTotal() throws Exception {

        double total =
                transactionService.calculateTotalAmount();

        System.out.println("Total transaction amount: " + total);
    }

    private void calculateTotalByType() throws Exception {

        System.out.println("1. DEPOSIT");
        System.out.println("2. WITHDRAWAL");
        System.out.println("3. TRANSFER");

        int choice = scanner.nextInt();
        scanner.nextLine();

        TransactionType type = switch (choice) {
            case 1 -> TransactionType.DEPOSIT;
            case 2 -> TransactionType.WITHDRAWAL;
            case 3 -> TransactionType.TRANSFER;
            default -> throw new IllegalArgumentException("Invalid type.");
        };

        double total =
                transactionService.calculateTotalByType(type);

        System.out.println(
                "Total " + type + ": " + total
        );
    }

    private void showSuspiciousTransactions() throws Exception {

        List<Transaction> suspicious =
                transactionService.detectSuspiciousTransactions();

        if (suspicious.isEmpty()) {
            System.out.println("No suspicious transactions found.");
            return;
        }

        System.out.println("Suspicious transactions:");

        suspicious.forEach(System.out::println);
    }

    private void updateTransaction() throws Exception {

        System.out.print("Transaction ID: ");
        Long id = scanner.nextLong();

        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("1. DEPOSIT");
        System.out.println("2. WITHDRAWAL");
        System.out.println("3. TRANSFER");

        int choice = scanner.nextInt();
        scanner.nextLine();

        TransactionType type = switch (choice) {
            case 1 -> TransactionType.DEPOSIT;
            case 2 -> TransactionType.WITHDRAWAL;
            case 3 -> TransactionType.TRANSFER;
            default -> throw new IllegalArgumentException("Invalid type.");
        };

        System.out.print("Location: ");
        String location = scanner.nextLine();

        System.out.print("Account ID: ");
        Long accountId = scanner.nextLong();
        scanner.nextLine();

        Transaction transaction = new Transaction(
                id,
                LocalDateTime.now(),
                amount,
                type,
                location,
                accountId
        );

        boolean updated =
                transactionService.update(transaction);

        System.out.println(
                updated
                        ? "Transaction updated."
                        : "Transaction not found."
        );
    }

    private void deleteTransaction() throws Exception {

        System.out.print("Transaction ID: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        boolean deleted =
                transactionService.deleteById(id);

        System.out.println(
                deleted
                        ? "Transaction deleted."
                        : "Transaction not found."
        );
    }

    private void createClient() throws Exception {

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        Client client = new Client(
            null,
            name,
            email
        );

        Client created = clientDAO.create(client);

        System.out.println("\nClient created!");
        System.out.println(created);
    }

    private void findClient() throws Exception {

        System.out.print("Client ID: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Client> result =
            clientDAO.findById(id);

        if (result.isPresent()) {

            System.out.println("\nClient found:");
            System.out.println(result.get());

        } else {

            System.out.println("Client not found.");
        }
    }

    private void listClients() throws Exception {

        List<Client> clients =
            clientDAO.findAll();

        if (clients.isEmpty()) {

            System.out.println("No clients found.");
            return;
        }

        System.out.println("\n---------- CLIENTS ----------");

        for (Client client : clients) {
            System.out.println(client);
        }
    }

    private void updateClient() throws Exception {

        System.out.print("Client ID: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("New name: ");
        String name = scanner.nextLine();

        System.out.print("New email: ");
        String email = scanner.nextLine();

        Client client = new Client(
            id,
            name,
            email
        );

        boolean updated =
            clientDAO.update(client);

        if (updated) {
            System.out.println("Client updated!");
        } else {
            System.out.println("Client not found.");
        }
    }

    private void deleteClient() throws Exception {

        System.out.print("Client ID: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        boolean deleted =
            clientDAO.deleteById(id);

        if (deleted) {
            System.out.println("Client deleted!");
        } else {
            System.out.println("Client not found.");
        }
    }

    private void accountMenu() {

        while (true) {

            System.out.println("\n---------- ACCOUNTS ----------");
            System.out.println("1. Create current account");
            System.out.println("2. Create savings account");
            System.out.println("3. Find account by ID");
            System.out.println("4. List all accounts");
            System.out.println("5. List accounts by client");
            System.out.println("6. Update account");
            System.out.println("7. Delete account");
            System.out.println("0. Back");

            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (choice) {

                    case 1 -> createCurrentAccount();

                    case 2 -> createSavingsAccount();

                    case 3 -> findAccount();

                    case 4 -> listAccounts();

                    case 5 -> listAccountsByClient();

                    case 6 -> updateAccount();

                    case 7 -> deleteAccount();

                    case 0 -> {
                        return;
                    }

                    default -> System.out.println("Invalid choice!");
                }

            } catch (Exception e) {

                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void createCurrentAccount() throws Exception {
        try{
            System.out.print("Account number: ");
            String number = scanner.nextLine();

            System.out.print("Initial balance: ");
            double balance = scanner.nextDouble();

            System.out.print("Client ID: ");
            long clientId = scanner.nextLong();

            System.out.print("Authorized overdraft: ");
            double overdraft = scanner.nextDouble();

            scanner.nextLine();

            CurrentAccount account =
                new CurrentAccount(
                    null,
                    number,
                    balance,
                    clientId,
                    overdraft
                );

            Account created =
                accountDAO.create(account);

            System.out.println("\nCurrent account created!");
            printAccount(created);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void createSavingsAccount() throws Exception {

        System.out.print("Account number: ");
        String number = scanner.nextLine();

        System.out.print("Initial balance: ");
        double balance = scanner.nextDouble();

        System.out.print("Client ID: ");
        long clientId = scanner.nextLong();

        System.out.print("Interest rate: ");
        double interestRate = scanner.nextDouble();

        scanner.nextLine();

        SavingsAccount account =
            new SavingsAccount(
                null,
                number,
                balance,
                clientId,
                interestRate
            );

        Account created =
            accountDAO.create(account);

        System.out.println("\nSavings account created!");
        printAccount(created);
    }

    private void findAccount() throws Exception {

        System.out.print("Account ID: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        Optional<Account> result =
            accountDAO.findById(id);

        if (result.isPresent()) {

            printAccount(result.get());

        } else {

            System.out.println("Account not found.");
        }
    }

    private void listAccounts() throws Exception {

        List<Account> accounts =
            accountDAO.findAll();

        if (accounts.isEmpty()) {

            System.out.println("No accounts found.");
            return;
        }

        for (Account account : accounts) {
            printAccount(account);
        }
    }

    private void listAccountsByClient() throws Exception {

        System.out.print("Client ID: ");
        long clientId = scanner.nextLong();
        scanner.nextLine();

        List<Account> accounts =
            accountDAO.findByClientId(clientId);

        if (accounts.isEmpty()) {

            System.out.println("No accounts found for this client.");
            return;
        }

        for (Account account : accounts) {
            printAccount(account);
        }
    }

    private void updateAccount() throws Exception {

        System.out.print("Account ID: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        System.out.print("Account number: ");
        String number = scanner.nextLine();

        System.out.print("Balance: ");
        double balance = scanner.nextDouble();

        System.out.print("Client ID: ");
        long clientId = scanner.nextLong();

        System.out.println("\n1. Current");
        System.out.println("2. Savings");
        System.out.print("Account type: ");

        int type = scanner.nextInt();

        Account account;

        if (type == 1) {

            System.out.print("Authorized overdraft: ");
            double overdraft = scanner.nextDouble();

            account = new CurrentAccount(
                id,
                number,
                balance,
                clientId,
                overdraft
            );

        } else {

            System.out.print("Interest rate: ");
            double interestRate = scanner.nextDouble();

            account = new SavingsAccount(
                id,
                number,
                balance,
                clientId,
                interestRate
            );
        }

        scanner.nextLine();

        boolean updated =
            accountDAO.update(account);

        if (updated) {
            System.out.println("Account updated!");
        } else {
            System.out.println("Account not found.");
        }
    }

    private void deleteAccount() throws Exception {

        System.out.print("Account ID: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        boolean deleted =
            accountDAO.deleteById(id);

        if (deleted) {
            System.out.println("Account deleted!");
        } else {
            System.out.println("Account not found.");
        }
    }

   private void printAccount(Account account) {

        System.out.println("\n-------------------------");
        System.out.println("ID: " + account.getId());
        System.out.println("Number: " + account.getNumber());
        System.out.println("Balance: " + account.getBalance());
        System.out.println("Client ID: " + account.getClientId());

        if (account instanceof CurrentAccount current) {

            System.out.println("Type: CURRENT");
            System.out.println(
                "Overdraft: " +
                current.getAuthorizedOverdraft()
            );

        } else if (account instanceof SavingsAccount savings) {

            System.out.println("Type: SAVINGS");
            System.out.println(
                "Interest rate: " +
                savings.getInterestRate()
            );
        }

        System.out.println("-------------------------");
    }
}