import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Product class that represents an item in the inventory.
 * This class extends AbstractItem and implements StockAdjustable.
 *
 * Main Features:
 * - Works for both perishable and non-perishable items
 * - Perishables are tracked in batches with their own expiration dates
 */
public class Product extends AbstractItem implements StockAdjustable {

    private String category; // What kind of product it is (e.g., Snacks, Dairy)
    private List<ProductBatch> batches; // Only used if the product is perishable

    /**
     * Constructor for Product
     * If it's perishable, we track quantity using batches with expiration dates.
     * If it's not perishable, we just store the quantity normally.
     */
    public Product(String name, String category, int quantity, LocalDate expirationDate, String section, boolean perishable) {
        super(name, 0, null, section, perishable); // Start with quantity 0 and set it based on perishable status
        this.category = category;
        this.batches = new ArrayList<>();

        if (perishable) {
            // Add the first batch of this perishable product
            addOrUpdateBatch(quantity, expirationDate);
        } else {
            this.quantity = quantity;
        }
    }

    /**
     * This method adds a new batch or updates an existing one (if the expiration date already exists)
     */
    public void addOrUpdateBatch(int qty, LocalDate expirationDate) {
        for (ProductBatch batch : batches) {
            if (batch.getExpirationDate().equals(expirationDate)) {
                batch.setQuantity(batch.getQuantity() + qty);
                return;
            }
        }
        // If no batch matched the expiration date, we add a new one
        batches.add(new ProductBatch(qty, expirationDate));
    }

    /**
     * Gets the total quantity of this product
     * If it's perishable, it adds up all batch quantities
     */
    @Override
    public int getQuantity() {
        if (!perishable) return quantity;
        return batches.stream().mapToInt(ProductBatch::getQuantity).sum();
    }

    /**
     * For perishable items, this gets the closest expiration date
     */
    @Override
    public LocalDate getExpirationDate() {
        if (!perishable) return null;
        return batches.stream()
                .map(ProductBatch::getExpirationDate)
                .min(LocalDate::compareTo)
                .orElse(null); // If no batches, return null
    }

    /**
     * Returns the product's category (like "Dairy" or "Clothing")
     */
    public String getCategory() {
        return category;
    }

    /**
     * Just tells the system this item is a "Product"
     */
    @Override
    public String getItemType() {
        return "Product";
    }

    /**
     * Returns all the batches for this perishable item
     */
    public List<ProductBatch> getBatches() {
        return batches;
    }

    /**
     * Adds stock to non-perishable products
     */
    @Override
    public void addStock(int amount) {
        if (!perishable) {
            this.quantity += amount;
        }
    }

    /**
     * Removes stock from non-perishable products
     * Throws an error if someone tries to remove more than we have
     */
    @Override
    public void removeStock(int amount) throws IllegalArgumentException {
        if (!perishable) {
            if (amount > this.quantity) throw new IllegalArgumentException("Not enough stock");
            this.quantity -= amount;
        }
    }

    /**
     * Directly sets a new quantity (non-perishable only)
     */
    public void updateStock(int newQuantity) {
        if (!perishable) {
            this.quantity = newQuantity;
        }
    }

    /**
     * Adds or subtracts quantity using operators like "+" or "-" (non-perishable)
     */
    public void updateStock(String operator, int value) {
        if (!perishable) {
            if (operator.equals("+")) this.quantity += value;
            else if (operator.equals("-")) this.quantity -= value;
        }
    }

    /**
     * Checks if all batches of this product are expired (for perishables only)
     */
    @Override
    public boolean isExpired() {
        if (!perishable) return false;
        LocalDate today = LocalDate.now();
        return batches.stream().allMatch(batch -> today.isAfter(batch.getExpirationDate()));
    }

    /**
     * Converts this product into a readable string
     * Shows batches if it's perishable
     */
    @Override
    public String toString() {
        if (!perishable) {
            return "Name: " + name + ", Quantity: " + quantity + ", Section: " + section + ", Type: Product";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Name: ").append(name).append(", Section: ").append(section).append(", Batches: [");
            for (ProductBatch batch : batches) {
                sb.append(batch.toString()).append(", ");
            }
            if (!batches.isEmpty()) sb.setLength(sb.length() - 2); // Remove last comma
            sb.append("]");
            return sb.toString();
        }
    }

    /**
     * Converts this product into CSV format (used for saving to file)
     */
    @Override
    public String toCSV() {
        if (!perishable) {
            return name + "," + quantity + ",N/A," + section + ",false";
        } else {
            ProductBatch first = batches.get(0);
            return name + "," + first.getQuantity() + "," + first.getExpirationDate() + "," + section + ",true";
        }
    }
}
