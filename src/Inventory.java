import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Inventory {
    private List<AbstractItem> items;
    private Map<String, AbstractItem> itemMap;
    private Stack<String> undoStack;
    private Queue<String> orderQueue;

    public static final int LOW_STOCK = 5;
    public static final int OVER_STOCK = 100;

    public Inventory() {
        items = new ArrayList<>();
        itemMap = new HashMap<>();
        undoStack = new Stack<>();
        orderQueue = new LinkedList<>();
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
            addItem(new Product(name, qty, expiration, section, true));
        }
    }

    public void updateStock(String name, int newQuantity) throws ProductNotFound {
        AbstractItem item = itemMap.get(name.toLowerCase());
        if (item == null) {
            throw new ProductNotFound("Item " + name + " not found.");
        }
        if (!item.isPerishable()) {
            item.setQuantity(item.getQuantity() + newQuantity);
        }
        undoStack.push("Updated " + name + " stock by adding " + newQuantity);
    }

    public void updateStock(String name, String operator, int value) throws ProductNotFound {
        AbstractItem item = itemMap.get(name.toLowerCase());
        if (item == null) {
            throw new ProductNotFound("Item " + name + " not found.");
        }
        if (item instanceof Product) {
            ((Product)item).updateStock(operator, value);
        }
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
        if (items.get(index).getName().equalsIgnoreCase(name)) {
            return items.get(index);
        }
        return recursiveSearch(name, index + 1);
    }

    public void displayItems() {
        System.out.println("Full Store Inventory:");
        Map<String, List<AbstractItem>> sectionMap = new HashMap<>();
        for (AbstractItem item : items) {
            sectionMap.computeIfAbsent(item.getSection(), k -> new ArrayList<>()).add(item);
        }
        for (String section : sectionMap.keySet()) {
            System.out.println("Section: " + section);
            for (AbstractItem item : sectionMap.get(section)) {
                System.out.println("  " + item.toString());
            }
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
                    AbstractItem item = new Product(name, quantity, expirationDate, section, perishable);
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
            System.out.println("Processing order: " + order);
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
