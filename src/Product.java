import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Product class that represents an item in the inventory.
 * This class extends AbstractItem and implements StockAdjustable.
 *
 * Main Features:
 * - Can handle both perishable and non-perishable products.
 * - Perishable products can have multiple batches with different expiration dates.
 */
public class Product extends AbstractItem implements StockAdjustable {

    // List of batches (used only if the product is perishable)
    private List<ProductBatch> batches;

    /**
     * Constructor for Product.
     * For perishable products, quantity is managed through batches.
     * For non-perishable products, quantity is stored directly.
     *
     * @param name Product name.
     * @param quantity Initial quantity.
     * @param expirationDate Expiration date (only if perishable).
     * @param section Store section.
     * @param perishable true if the product is perishable.
     */
    public Product(String name, int quantity, LocalDate expirationDate, String section, boolean perishable) {
        super(name, 0, null, section, perishable); // Quantity is set based on perishable status
        this.batches = new ArrayList<>();

        // If perishable, create a batch
        if (perishable) {
            addOrUpdateBatch(quantity, expirationDate);
        } else {
            this.quantity = quantity; // For non-perishable items
        }
    }

    /**
     * Adds a new batch OR updates an existing batch (if expiration date matches).
     *
     * @param qty Quantity to add.
     * @param expirationDate Expiration date of the batch.
     */
    public void addOrUpdateBatch(int qty, LocalDate expirationDate) {
        for (ProductBatch batch : batches) {
            if (batch.getExpirationDate().equals(expirationDate)) {
                batch.setQuantity(batch.getQuantity() + qty);
                return;
            }
        }
        batches.add(new ProductBatch(qty, expirationDate));
    }

    /**
     * Calculates total quantity of product.
     * For perishable: sum of all batches.
     * For non-perishable: stored quantity.
     */
    @Override
    public int getQuantity() {
        if (!perishable) return quantity;
        return batches.stream().mapToInt(ProductBatch::getQuantity).sum();
    }

    /**
     * Returns the earliest expiration date (for perishable products).
     */
    @Override
    public LocalDate getExpirationDate() {
        if (!perishable) return null;
        return batches.stream()
                .map(ProductBatch::getExpirationDate)
                .min(LocalDate::compareTo)
                .orElse(null);
    }

    // Getter for all batches
    public List<ProductBatch> getBatches() {
        return batches;
    }

    /**
     * Tells us this is a Product type item.
     */
    @Override
    public String getItemType() {
        return "Product";
    }

    /**
     * Adds stock (only for non-perishable).
     */
    @Override
    public void addStock(int amount) {
        if (!perishable) {
            this.quantity += amount;
        }
    }

    /**
     * Removes stock (only for non-perishable).
     * Throws error if trying to remove more than available.
     */
    @Override
    public void removeStock(int amount) throws IllegalArgumentException {
        if (!perishable) {
            if (amount > this.quantity) throw new IllegalArgumentException("Not enough stock");
            this.quantity -= amount;
        }
    }

    /**
     * Update stock with a new quantity (for non-perishable).
     */
    public void updateStock(int newQuantity) {
        if (!perishable) {
            this.quantity = newQuantity;
        }
    }

    /**
     * Update stock using an operator (+ or -) and a value.
     */
    public void updateStock(String operator, int value) {
        if (!perishable) {
            if (operator.equals("+")) this.quantity += value;
            else if (operator.equals("-")) this.quantity -= value;
        }
    }

    /**
     * Checks if all batches are expired (for perishable).
     */
    @Override
    public boolean isExpired() {
        if (!perishable) return false;
        LocalDate today = LocalDate.now();
        return batches.stream().allMatch(batch -> today.isAfter(batch.getExpirationDate()));
    }

    /**
     * String version of Product.
     * For perishable: shows all batches.
     * For non-perishable: shows quantity.
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
     * Converts product data to CSV format for file saving.
     * For perishable: uses first batch details.
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
