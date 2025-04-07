import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for Team4$ SuperMinimarket Inventory Management System.
 * Displays today's date, alerts the manager to perishable items expiring within 7 days,
 * allows a manager to log in by selecting their name, and provides a menu to manage inventory.
 * Additionally, it provides private GM-only information accessible only to Andrea.
 */
public class Main {
    /**
     * Prints an ASCII art banner with the store name and today's date.
     */
    public static void printBanner() {
        System.out.println("========================================");
        System.out.println("   Welcome to Team4$ SuperMinimarket  ");
        System.out.println("========================================");
        System.out.println("Today's Date: " + LocalDate.now());
    }

    /**
     * Allows the user to choose a manager from a predefined list.
     * @param scanner Scanner object for user input.
     * @return The name of the chosen manager.
     */
    public static String managerLogin(Scanner scanner) {
        List<String> managers = Arrays.asList("Andrea", "Mohamed", "Dylan", "Ryan");
        System.out.println("\nManager Login");
        System.out.println("Available managers: " + managers);
        while(true) {
            System.out.print("Please enter your name to log in: ");
            String input = scanner.nextLine().trim();
            for(String manager : managers) {
                if(manager.equalsIgnoreCase(input)) {
                    System.out.println("Welcome, " + manager + "!");
                    return manager;
                }
            }
            System.out.println("❗ Invalid manager name. Try again.");
        }
    }

    /**
     * Generates allowed expiration dates.
     * For example, starting at 3 weeks (21 days) from today and spanning 7 days.
     * @return A list of allowed LocalDate objects.
     */
    public static List<LocalDate> generateAllowedDates() {
        List<LocalDate> allowedDates = new ArrayList<>();
        LocalDate startDate = LocalDate.now().plusDays(21);
        // Generate 7 consecutive dates starting at startDate.
        for (int i = 0; i < 7; i++) {
            allowedDates.add(startDate.plusDays(i));
        }
        return allowedDates;
    }

    /**
     * Prepopulates the inventory with sample products.
     * Some items are perishable (Vegetables & Fruits, Cereals & Snacks, Dairy) and others are non-perishable (Electronics, Clothing, Toys).
     */
    public static void initializeInventory(Inventory inventory) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // For prepopulated products, we assume dates are valid.
        inventory.addItem(new Product("Apple", 50, LocalDate.parse("2025-05-10", formatter), "Vegetables & Fruits", true));
        inventory.addItem(new Product("Banana", 40, LocalDate.parse("2025-05-08", formatter), "Vegetables & Fruits", true));
        inventory.addItem(new Product("Corn Flakes", 30, LocalDate.parse("2025-05-20", formatter), "Cereals & Snacks", true));
        inventory.addItem(new Product("Potato Chips", 60, LocalDate.parse("2025-05-18", formatter), "Cereals & Snacks", true));
        inventory.addItem(new Product("Milk", 20, LocalDate.parse("2025-05-15", formatter), "Dairy", true));
        inventory.addItem(new Product("Yogurt", 25, LocalDate.parse("2025-05-16", formatter), "Dairy", true));
        inventory.addItem(new Product("LED TV", 10, null, "Electronics", false));
        inventory.addItem(new Product("T-Shirt", 35, null, "Clothing", false));
        inventory.addItem(new Product("Action Figure", 15, null, "Toys", false));
    }

    /**
     * Displays additional private information for the GM (Andrea).
     * This report shows total product count and low stock items.
     */
    public static void displayPrivateGMInfo(Inventory inventory) {
        System.out.println("========== GM Private Report ==========");
        System.out.println("Total number of products: " + inventory.getTotalItemCount());
        System.out.println("Total low stock items: " + inventory.getLowStockCount());
        System.out.println("=========================================");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Inventory inventory = new Inventory();
        Warehouse<AbstractItem> warehouse = new Warehouse<>();

        printBanner();

        // Manager login process.
        String manager = managerLogin(scanner);

        // Prepopulate inventory.
        initializeInventory(inventory);

        // Alert for perishable items expiring within 7 days.
        inventory.checkExpiringItems();

        // Main menu loop.
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Add a New Product");
            System.out.println("2. Update Product Quantity");
            System.out.println("3. Display Items by Section");
            System.out.println("4. Process Orders");
            System.out.println("5. Undo Last Update");
            // GM-only option visible only if manager is Andrea.
            if(manager.equalsIgnoreCase("Andrea")) {
                System.out.println("7. View GM Private Report");
            }
            System.out.println("6. Save & Exit");
            System.out.print("Your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch(NumberFormatException e) {
                System.out.println("❗ Please enter a valid number.");
                continue;
            }

            // GM-specific option.
            if(manager.equalsIgnoreCase("Andrea") && choice == 7) {
                displayPrivateGMInfo(inventory);
                continue;
            }

            if(choice == 6) {
                try {
                    inventory.saveInventory("inventory.txt");
                    System.out.println(" Inventory saved. Exiting system... Goodbye, " + manager + "!");
                } catch(IOException e) {
                    System.out.println("❗ Error saving inventory: " + e.getMessage());
                }
                return;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter quantity: ");
                    int quantity;
                    try {
                        quantity = Integer.parseInt(scanner.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.println("❗ Invalid quantity.");
                        break;
                    }

                    boolean perishable;
                    LocalDate expirationDate = null;
                    System.out.print("Is this product perishable? (yes/no): ");
                    String perishableInput = scanner.nextLine();
                    perishable = perishableInput.equalsIgnoreCase("yes");

                    if(perishable) {
                        // Generate allowed expiration dates dynamically.
                        List<LocalDate> allowedDates = generateAllowedDates();
                        System.out.println("Select an expiration date from the following options:");
                        for (int i = 0; i < allowedDates.size(); i++) {
                            System.out.println((i + 1) + ". " + allowedDates.get(i));
                        }
                        System.out.print("Enter the number corresponding to your chosen date: ");
                        int indexChoice;
                        try {
                            indexChoice = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("❗ Invalid selection.");
                            break;
                        }
                        if(indexChoice < 1 || indexChoice > allowedDates.size()) {
                            System.out.println("❗ Invalid selection.");
                            break;
                        }
                        expirationDate = allowedDates.get(indexChoice - 1);
                        // The generated allowed dates are by design at least 3 weeks from today.
                    }

                    System.out.print("Enter store section (e.g., Vegetables & Fruits, Cereals & Snacks, Dairy, Electronics, Clothing, Toys): ");
                    String section = scanner.nextLine();

                    Product newProduct = new Product(name, quantity, expirationDate, section, perishable);
                    inventory.addItem(newProduct);
                    warehouse.addItem(newProduct);
                    System.out.println(" Product added!");

                    // If perishable and expiring soon (within 7 days), show an alert.
                    if(perishable && newProduct.getExpirationDate().isBefore(LocalDate.now().plusDays(8))) {
                        System.out.println(" Alert: " + newProduct.getName() + " will expire on " + newProduct.getExpirationDate());
                    }
                    break;

                case 2:
                    System.out.print("Enter product name to update: ");
                    String updateName = scanner.nextLine();

                    // Display product details before update.
                    AbstractItem product = inventory.getItemByName(updateName);
                    if(product == null) {
                        System.out.println("❗ Product not found.");
                        break;
                    }
                    System.out.println("Before update: " + product.toString());

                    System.out.print("Enter new quantity: ");
                    int newQuantity;
                    try {
                        newQuantity = Integer.parseInt(scanner.nextLine());
                    } catch(NumberFormatException e) {
                        System.out.println("❗ Invalid quantity.");
                        break;
                    }
                    try {
                        inventory.updateStock(updateName, newQuantity);
                        System.out.println(" Quantity updated!");
                    } catch (ProductNotFound e) {
                        System.out.println("❗ " + e.getMessage());
                        break;
                    }

                    // Display product details after update.
                    AbstractItem updated = inventory.getItemByName(updateName);
                    System.out.println("After update: " + updated.toString());
                    break;

                case 3:
                    inventory.displayItems();
                    break;

                case 4:
                    System.out.print("Enter order details: ");
                    String order = scanner.nextLine();
                    inventory.addOrder(order);
                    inventory.processOrders();
                    break;

                case 5:
                    inventory.undoLastUpdate();
                    break;

                default:
                    System.out.println("❗ Invalid option. Please try again.");
            }
        }
    }
}
