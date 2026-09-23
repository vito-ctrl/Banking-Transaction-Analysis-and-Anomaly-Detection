package ui;

// import model.Subscription;
// import model.Payment;
// import services.SubscriptionService;
// import services.PaymentService;
// import dao.SubscriptionDAO;
// import dao.PaymentDAO;
// import utils.ValidateInput;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Menu {

    private Scanner scanner;
    // private SubscriptionService subscriptionService;
    // private PaymentService paymentService;

    public Menu() {
        scanner = new Scanner(System.in);
    }

    public void start() {
        int choice;

        do {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    // case 1: createSubscription(); break;
                    case 0: System.out.println("\nGoodbye!"); break;
                    default: System.out.println("\nInvalid choice!");
                }
            } catch (IllegalArgumentException e) {
                // System.out.println("Error: " + e.getMessage());
            }

        } while (choice != 0);
    }

    private void displayMenu() {
        System.out.println("\n======================================");
        System.out.println("       Stats MANAGEMENT");
        System.out.println("======================================");
        System.out.println("\n--- SUBSCRIPTIONS ---");
        System.out.println("1. Create subscription");
        System.out.println("2. Modify subscription");
        System.out.println("3. Delete subscription");
        System.out.println("4. List subscriptions");
        System.out.println("\n--- PAYMENTS ---");
        System.out.println("5. Show subscription payments");
        System.out.println("6. Record payment");
        System.out.println("7. Modify payment");
        System.out.println("8. Delete payment");
        System.out.println("9. Show total paid");
        System.out.println("\n0. Exit");
        System.out.print("\nChoose an option: ");
    }
}