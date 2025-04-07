import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * class for managing the store inventory.
 * Uses collections, file I/O, recursion, and provides helper methods for reporting.
 */
public class Inventory {
    private List<AbstractItem> items;
    private Map<String, AbstractItem> itemMap;
    private Stack<String> undoStack;      // For undo actions.
    private Queue<String> orderQueue;     // For processing orders.

    public static final int LOW_STOCK = 5;
    public static final int OVER_STOCK = 100;

    public Inventory() {
        items = new ArrayList<>();
        itemMap = new HashMap<>();
        undoStack = new Stack<>();
        orderQueue = new LinkedList<>();
    }

    /**
     * Adds an item to the inventory.
     * @param item The item to add.
     */
    public void addItem(AbstractItem item) {
        items.add(item);
        itemMap.put(item.getName().toLowerCase(), item);
    }

    /**
     * Updates stock by setting a new quantity.
     * @param name Name of the item.
     * @param newQuantity New quantity.
     * @throws ProductNotFound if the item is not found.
     */
    public void updateStock(String name, int newQuantity) throws ProductNotFound {
        AbstractItem item = itemMap.get(name.toLowerCase());
        if(item == null) {
            throw new ProductNotFound("Item " + name + " not found.");
        }
        item.setQuantity(newQuantity);
        undoStack.push("Updated " + name + " stock to " + newQuantity);
    }

    /**
     * Updates stock using an operator.
     * @param name Name of the item.
     * @param operator "+" to add, "-" to subtract.
     * @param value Value to update.
     * @throws ProductNotFound if the item is not found.
     */
    public void updateStock(String name, String operator, int value) throws ProductNotFound {
        AbstractItem item = itemMap.get(name.toLowerCase());
        if(item == null) {
            throw new ProductNotFound("Item " + name + " not found.");
        }
        if(item instanceof Product) {
            ((Product)item).updateStock(operator, value);
        } else {
            int newQty = operator.equals("+") ? item.getQuantity() + value :
                    operator.equals("-") ? item.getQuantity() - value : item.getQuantity();
            item.setQuantity(newQty);
        }
        undoStack.push("Updated " + name + " stock with operator " + operator + " and value " + value);
    }

    /**
     * Retrieves an item by name (case-insensitive).
     * @param name The name of the item.
     * @return The matching item, or null if not found.
     */
    public AbstractItem getItemByName(String name) {
        return itemMap.get(name.toLowerCase());
    }

    /**
     * Checks for items with low stock.
     */
    public void checkLowStock() {
        System.out.println(" Checking for low stock items...");
        for(AbstractItem item : items) {
            if(item.getQuantity() < LOW_STOCK) {
                System.out.println("Low stock: " + item);
            }
        }
    }

    /**
     * Checks for items with over stock.
     */
    public void checkOverStock() {
        System.out.println(" Checking for overstock items...");
        for(AbstractItem item : items) {
            if(item.getQuantity() > OVER_STOCK) {
                System.out.println("Overstock: " + item);
            }
        }
    }

    /**
     * Recursively searches for an item by name.
     * @param name The name to search for.
     * @param index The starting index.
     * @return The found item or null if not found.
     */
    public AbstractItem recursiveSearch(String name, int index) {
        if(index >= items.size()) return null;
        if(items.get(index).getName().equalsIgnoreCase(name)) {
            return items.get(index);
        }
        return recursiveSearch(name, index + 1);
    }

    /**
     * Displays all items in the inventory grouped by section.
     */
    public void displayItems() {
        System.out.println(" Full Store Inventory:");
        Map<String, List<AbstractItem>> sectionMap = new HashMap<>();
        for(AbstractItem item : items) {
            sectionMap.computeIfAbsent(item.getSection(), k -> new ArrayList<>()).add(item);
        }
        for(String section : sectionMap.keySet()) {
            System.out.println("Section: " + section);
            for(AbstractItem item : sectionMap.get(section)) {
                System.out.println("  " + item.toString());
            }
        }
    }

    /**
     * Saves the inventory to a file in CSV format.
     * @param filename The file to save to.
     * @throws IOException if an I/O error occurs.
     */
    public void saveInventory(String filename) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for(AbstractItem item : items) {
                writer.write(item.toCSV());
                writer.newLine();
            }
        }
    }

    /**
     * Loads the inventory from a CSV file.
     * Expected CSV format: name,quantity,expirationDate,section,perishable
     * @param filename The file to load from.
     * @throws IOException if an I/O error occurs.
     */
    public void loadInventory(String filename) throws IOException {
        File file = new File(filename);
        if(!file.exists()) return;
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length == 5) {
                    String name = parts[0];
                    int quantity = Integer.parseInt(parts[1]);
                    String expStr = parts[2];
                    String section = parts[3];
                    boolean perishable = Boolean.parseBoolean(parts[4]);
                    LocalDate expirationDate = perishable ? LocalDate.parse(expStr) : null;
                    AbstractItem item = new Product(name, quantity, expirationDate, section, perishable);
                    addItem(item);
                }
            }
        }
    }

    /**
     * Adds an order to the processing queue.
     * @param order Order details.
     */
    public void addOrder(String order) {
        orderQueue.offer(order);
    }

    /**
     * Processes orders from the queue.
     */
    public void processOrders() {
        System.out.println(" Processing orders...");
        while(!orderQueue.isEmpty()) {
            String order = orderQueue.poll();
            System.out.println("Processing order: " + order);
        }
    }

    /**
     * Undoes the last update action.
     */
    public void undoLastUpdate() {
        if(!undoStack.isEmpty()) {
            String lastAction = undoStack.pop();
            System.out.println(" Undoing action: " + lastAction);
        } else {
            System.out.println("Nothing to undo!");
        }
    }

    /**
     * Checks for perishable items expiring within the next 7 days and prints an alert.
     */
    public void checkExpiringItems() {
        LocalDate today = LocalDate.now();
        LocalDate weekLater = today.plusDays(7);
        System.out.println(" Alert: Perishable items expiring within the next 7 days:");
        boolean found = false;
        for(AbstractItem item : items) {
            if(item.isPerishable() &&
                    !item.isExpired() &&
                    (item.getExpirationDate().isEqual(today) ||
                            (item.getExpirationDate().isAfter(today) && item.getExpirationDate().isBefore(weekLater.plusDays(1))))) {
                System.out.println("  " + item.getName() + " (Section: " + item.getSection() + ") - Expires on " + item.getExpirationDate() + " (Qty: " + item.getQuantity() + ")");
                found = true;
            }
        }
        if(!found) {
            System.out.println("  No perishable items expiring within the next 7 days.");
        }
    }

    /**
     * Returns the total count of items in the inventory.
     */
    public int getTotalItemCount() {
        return items.size();
    }

    /**
     * Returns the count of items with low stock.
     */
    public int getLowStockCount() {
        int count = 0;
        for(AbstractItem item : items) {
            if(item.getQuantity() < LOW_STOCK) {
                count++;
            }
        }
        return count;
    }
}
