import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void printBanner() {
        System.out.println("========================================");
        System.out.println("  Welcome to Team4$ SuperMinimarket  ");
      //  System.out.println("       Management Extravaganza        ");
        System.out.println("========================================");
        System.out.println("Today's Date: " + LocalDate.now());
    }

    public static String managerLogin(Scanner scanner) {
        List<String> managers = Arrays.asList("Andrea", "Mohamed", "Dylan", "Ryan");
        System.out.println("\nManager Login");
        System.out.println("Available managers: " + managers);
        while (true) {
            System.out.print("Please enter your name to log in: ");
            String input = scanner.nextLine().trim();
            for (String manager : managers) {
                if (manager.equalsIgnoreCase(input)) {
                    System.out.println("Welcome, " + manager + "!");
                    return manager;
                }
            }
            System.out.println("Invalid manager name. Try again.");
        }
    }

    public static List<LocalDate> generateAllowedDates() {
        List<LocalDate> allowedDates = new ArrayList<>();
        LocalDate startDate = LocalDate.now().plusDays(21);
        for (int i = 0; i < 7; i++) {
            allowedDates.add(startDate.plusDays(i));
        }
        return allowedDates;
    }

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
        String manager = managerLogin(scanner);
        inventory.checkExpiringItems();

        // Categorize products by section
        Map<String, List<String>> sectionProducts = new LinkedHashMap<>();
        sectionProducts.put("Vegetables & Fruits", Arrays.asList("Apple", "Banana"));
        sectionProducts.put("Cereals & Snacks", Arrays.asList("Corn Flakes", "Potato Chips"));
        sectionProducts.put("Dairy", Arrays.asList("Milk", "Yogurt"));
        sectionProducts.put("Electronics", Arrays.asList("LED TV"));
        sectionProducts.put("Clothing", Arrays.asList("T-Shirt"));
        sectionProducts.put("Toys", Arrays.asList("Action Figure"));

        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Add a New Product");
            System.out.println("2. Update Product Quantity");
            System.out.println("3. Display Items by Section");
            System.out.println("4. Process Orders");
            System.out.println("5. Undo Last Update");
            if (manager.equalsIgnoreCase("Andrea")) {
                System.out.println("7. View GM Private Report");
            }
            System.out.println("6. Save & Exit");
            System.out.print("Your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            if (manager.equalsIgnoreCase("Andrea") && choice == 7) {
                displayPrivateGMInfo(inventory);
                continue;
            }

            if (choice == 6) {
                try {
                    inventory.saveInventory("inventory.txt");
                    System.out.println("Inventory saved. Exiting system... Goodbye, " + manager + "!");
                } catch (IOException e) {
                    System.out.println("Error saving inventory: " + e.getMessage());
                }
                return;
            }

            switch (choice) {
                case 1:
                    List<String> allSections = new ArrayList<>(sectionProducts.keySet());
                    System.out.println("Select a section:");
                    for (int i = 0; i < allSections.size(); i++) {
                        System.out.println((i + 1) + ". " + allSections.get(i));
                    }
                    System.out.print("Enter section number: ");
                    int sectionChoice;
                    try {
                        sectionChoice = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid section.");
                        break;
                    }
                    if (sectionChoice < 1 || sectionChoice > allSections.size()) {
                        System.out.println("Invalid section.");
                        break;
                    }
                    String section = allSections.get(sectionChoice - 1);
                    List<String> availableProducts = sectionProducts.get(section);

                    System.out.println("Select a product from " + section + ":");
                    for (int i = 0; i < availableProducts.size(); i++) {
                        System.out.println((i + 1) + ". " + availableProducts.get(i));
                    }
                    System.out.print("Enter product number: ");
                    int productChoice;
                    try {
                        productChoice = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid selection.");
                        break;
                    }
                    if (productChoice < 1 || productChoice > availableProducts.size()) {
                        System.out.println("Invalid selection.");
                        break;
                    }
                    String name = availableProducts.get(productChoice - 1);

                    System.out.print("Enter quantity: ");
                    int quantity;
                    try {
                        quantity = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity.");
                        break;
                    }

                    boolean perishable;
                    LocalDate expirationDate = null;
                    System.out.print("Is this product perishable? (yes/no): ");
                    String perishableInput = scanner.nextLine();
                    perishable = perishableInput.equalsIgnoreCase("yes");

                    if (perishable) {
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
                            System.out.println("Invalid selection.");
                            break;
                        }
                        if (indexChoice < 1 || indexChoice > allowedDates.size()) {
                            System.out.println("Invalid selection.");
                            break;
                        }
                        expirationDate = allowedDates.get(indexChoice - 1);
                    }

                    Product newProduct = new Product(name, quantity, expirationDate, section, perishable);
                    inventory.addItem(newProduct);
                    warehouse.addItem(newProduct);
                    System.out.println("Product added.");

                    break;

                case 2:
                    List<AbstractItem> productList = inventory.getAllItems();
                    if (productList.isEmpty()) {
                        System.out.println("No products available to update.");
                        break;
                    }
                    System.out.println("Select a product to update:");
                    for (int i = 0; i < productList.size(); i++) {
                        AbstractItem item = productList.get(i);
                        System.out.println((i + 1) + ". " + item.getName() + " (Qty: " + item.getQuantity() + ")");
                    }
                    System.out.print("Enter the product number: ");
                    int prodChoice;
                    try {
                        prodChoice = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid selection.");
                        break;
                    }
                    if (prodChoice < 1 || prodChoice > productList.size()) {
                        System.out.println("Invalid selection.");
                        break;
                    }
                    AbstractItem product = productList.get(prodChoice - 1);
                    System.out.println("Before update: " + product.toString());

                    System.out.print("Enter new quantity: ");
                    int newQuantity;
                    try {
                        newQuantity = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity.");
                        break;
                    }
                    try {
                        inventory.updateStock(product.getName(), newQuantity);
                        System.out.println("Quantity updated.");
                    } catch (ProductNotFound e) {
                        System.out.println(e.getMessage());
                        break;
                    }

                    AbstractItem updated = inventory.getItemByName(product.getName());
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
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
