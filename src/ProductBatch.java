import java.time.LocalDate;

/**
 * Represents a batch of a perishable product with a specific expiration date.
 * Each batch has its own quantity and expiration, which allows multiple
 * batches of the same product to be tracked separately.
 */
public class ProductBatch {

    // Quantity in this specific batch
    private int quantity;

    // Expiration date for this batch
    private LocalDate expirationDate;

    /**
     * Constructor to initialize a product batch with quantity and expiration date.
     *
     * @param quantity         How many items are in this batch
     * @param expirationDate   When this batch will expire
     */
    public ProductBatch(int quantity, LocalDate expirationDate) {
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }

    // Returns the quantity in this batch
    public int getQuantity() {
        return quantity;
    }

    // Updates the quantity in this batch
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Returns the expiration date of this batch
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * Provides a string version of this batch.
     * Example: "Qty: 10, Exp: 2025-04-30"
     */
    @Override
    public String toString() {
        return "Qty: " + quantity + ", Exp: " + expirationDate;
    }
}
