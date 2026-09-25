package ui;

import dao.AccountDAO;
import dao.ClientDAO;
import model.Account;
import model.Client;
import model.CurrentAccount;
import model.SavingsAccount;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);

    private final ClientDAO clientDAO = new ClientDAO();
    private final AccountDAO accountDAO = new AccountDAO();


    public void start() {

        while (true) {

            System.out.println("\n==============================");
            System.out.println("     BANKING ANALYSIS");
            System.out.println("==============================");
            System.out.println("1. Client management");
            System.out.println("2. Account management");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1 -> clientMenu();

                case 2 -> accountMenu();

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