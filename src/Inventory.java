import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Inventory class manages the store items.
 * Handles everything like adding items, updating stock,
 * saving to file, loading from file, and more.
 * Also tracks how many items each manager adds!
 */
public class Inventory {

    private List<AbstractItem> items; // List of all products in the store
    private Map<String, AbstractItem> itemMap; // Helps us find products faster
    private Stack<String> undoStack; // Used for undoing the last stock update
    private Queue<String> orderQueue; // Stores customer orders

    // Keep track of how much each manager has added
    private Map<String, Integer> managerContributions = new HashMap<>();

    public static final int LOW_STOCK = 5;
    public static final int OVER_STOCK = 100;

    // Constructor
    public Inventory() {
        items = new ArrayList<>();
        itemMap = new HashMap<>();
        undoStack = new Stack<>();
        orderQueue = new LinkedList<>();
    }

    public void recordManagerContribution(String manager, int quantity) {
        managerContributions.put(manager, managerContributions.getOrDefault(manager, 0) + quantity);
    }

    public Map<String, Integer> getManagerContributions() {
        return managerContributions;
    }

    public void addItem(AbstractItem item) {
        items.add(item);
        itemMap.put(item.getName().toLowerCase(), item);
    }

    public void addOrUpdatePerishable(String name, int qty, LocalDate expiration, String section) {
        AbstractItem existing = itemMap.get(name.toLowerCase());
        if (existing != null && existing instanceof Product && existing.isPerishable()) {
            ((Product) existing).addOrUpdateBatch(qty, expiration);
        } else {
            addItem(new Product(name, "General", qty, expiration, section, true));
        }
    }

    public void printMostStockedProduct() {
        int maxQty = 0;
        String productName = "";

        for (AbstractItem item : items) {
            if (item.getQuantity() > maxQty) {
                maxQty = item.getQuantity();
                productName = item.getName();
            }
        }

        System.out.println("Most Stocked: " + productName + " with " + Math.max(maxQty, 0) + " units");
    }

    public void updateStock(String name, int newQuantity) throws ProductNotFound {
        AbstractItem item = itemMap.get(name.toLowerCase());
        if (item == null) throw new ProductNotFound("Item " + name + " not found.");
        if (!item.isPerishable()) item.setQuantity(item.getQuantity() + newQuantity);
        undoStack.push("Updated " + name + " stock by adding " + newQuantity);
    }

    public void updateStock(String name, String operator, int value) throws ProductNotFound {
        AbstractItem item = itemMap.get(name.toLowerCase());
        if (item == null) throw new ProductNotFound("Item " + name + " not found.");
        if (item instanceof Product) ((Product)item).updateStock(operator, value);
        undoStack.push("Updated " + name + " stock with operator " + operator + " and value " + value);
    }

    public AbstractItem getItemByName(String name) {
        return itemMap.get(name.toLowerCase());
    }

    public List<AbstractItem> getAllItems() {
        return items;
    }

    public void checkLowStock() {
        System.out.println("Checking for low stock items:");
        for (AbstractItem item : items) {
            if (item.getQuantity() < LOW_STOCK) {
                System.out.println("Low stock: " + item);
            }
        }
    }

    public void checkOverStock() {
        System.out.println("Checking for overstock items:");
        for (AbstractItem item : items) {
            if (item.getQuantity() > OVER_STOCK) {
                System.out.println("Overstock: " + item);
            }
        }
    }

    public AbstractItem recursiveSearch(String name, int index) {
        if (index >= items.size()) return null;
        if (items.get(index).getName().equalsIgnoreCase(name)) return items.get(index);
        return recursiveSearch(name, index + 1);
    }

    public Map<String, List<Product>> getProductsGroupedByCategory() {
        Map<String, List<Product>> grouped = new HashMap<>();
        for (AbstractItem item : items) {
            if (item instanceof Product) {
                Product p = (Product) item;
                grouped.computeIfAbsent(p.getCategory(), k -> new ArrayList<>()).add(p);
            }
        }
        return grouped;
    }

    public void displayItems() {
        System.out.println("Full Store Inventory:");
        Map<String, List<AbstractItem>> sectionMap = new HashMap<>();

        for (AbstractItem item : items) {
            sectionMap.computeIfAbsent(item.getSection(), k -> new ArrayList<>()).add(item);
        }

        for (String section : sectionMap.keySet()) {
            System.out.println("Section: " + section);
            int totalQty = 0;

            for (AbstractItem item : sectionMap.get(section)) {
                System.out.println("  " + item.toString());
                totalQty += item.getQuantity();
            }

            System.out.println("Total quantity of products in " + section + ": " + totalQty);
        }

        System.out.println("\n--- Manager Product Contributions ---");
        for (String manager : managerContributions.keySet()) {
            System.out.println(manager + " added a total of " + managerContributions.get(manager) + " products.");
        }
    }

    public void saveInventory(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (AbstractItem item : items) {
                writer.write(item.toCSV());
                writer.newLine();
            }
        }
    }

    public void loadInventory(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String name = parts[0];
                    int quantity = Integer.parseInt(parts[1]);
                    String expStr = parts[2];
                    String section = parts[3];
                    boolean perishable = Boolean.parseBoolean(parts[4]);
                    LocalDate expirationDate = perishable ? LocalDate.parse(expStr) : null;

                    // FIX: using "General" as the default category
                    AbstractItem item = new Product(name, "General", quantity, expirationDate, section, perishable);
                    addItem(item);
                }
            }
        }
    }

    public void addOrder(String order) {
        orderQueue.offer(order);
    }
    public void processOrders() {
        System.out.println("Processing orders:");
        while (!orderQueue.isEmpty()) {
            String order = orderQueue.poll();
            String[] parts = order.trim().split(" ");

            if (parts.length < 2) {
                System.out.println("❌ Invalid order format. Use format like '3 apples'");
                continue;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid quantity in order: " + order);
                continue;
            }

            // Reconstruct product name (handles multi-word names like "apple juice")
            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                nameBuilder.append(parts[i]).append(" ");
            }
            String rawName = nameBuilder.toString().trim();

            // Try both singular and plural matches
            AbstractItem item = getItemByName(rawName);
            if (item == null && rawName.endsWith("s")) {
                item = getItemByName(rawName.substring(0, rawName.length() - 1));
            }

            if (item == null) {
                System.out.println(" Product not found: " + rawName);
                continue;
            }

            if (item.getQuantity() < quantity) {
                System.out.println(" Not enough stock to fulfill order for: " + rawName + " (Requested: " + quantity + ", Available: " + item.getQuantity() + ")");
                continue;
            }

            if (item instanceof Product) {
                Product product = (Product) item;
                try {
                    for (int i = 0; i < quantity; i++) {
                        product.removeStock(1);
                    }
                    System.out.println(" Sold " + quantity + " unit(s) of " + product.getName() + " (Remaining: " + product.getQuantity() + ")");
                } catch (IllegalArgumentException e) {
                    System.out.println(" Error processing order for: " + rawName);
                }
            } else {
                System.out.println(" Cannot process non-product item.");
            }
        }
    }



    public void undoLastUpdate() {
        if (!undoStack.isEmpty()) {
            String lastAction = undoStack.pop();
            System.out.println("Undoing action: " + lastAction);
        } else {
            System.out.println("Nothing to undo!");
        }
    }

    public void checkExpiringItems() {
        LocalDate today = LocalDate.now();
        LocalDate weekLater = today.plusDays(7);
        System.out.println("Alert: Perishable items expiring within the next 7 days:");
        boolean found = false;
        for (AbstractItem item : items) {
            if (item.isPerishable() && item instanceof Product) {
                for (ProductBatch batch : ((Product) item).getBatches()) {
                    if (!today.isAfter(batch.getExpirationDate()) && batch.getExpirationDate().isBefore(weekLater.plusDays(1))) {
                        System.out.println("  " + item.getName() + " (Section: " + item.getSection() + ") - Expires on " + batch.getExpirationDate() + " (Qty: " + batch.getQuantity() + ")");
                        found = true;
                    }
                }
            }
        }
        if (!found) {
            System.out.println("  No perishable items expiring within the next 7 days.");
        }
    }

    public int getTotalItemCount() {
        return items.size();
    }

    public int getLowStockCount() {
        int count = 0;
        for (AbstractItem item : items) {
            if (item.getQuantity() < LOW_STOCK) {
                count++;
            }
        }
        return count;
    }
}
