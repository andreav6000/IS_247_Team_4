import java.time.LocalDate;

/**
 * This class represents a single batch of a perishable product.
 * Each batch has its own quantity and expiration date.
 */
public class ProductBatch {
    private int quantity;
    private LocalDate expirationDate;

    /**
     * Constructor for a product batch.
     * @param quantity how many units are in this batch
     * @param expirationDate when this batch expires
     */
    public ProductBatch(int quantity, LocalDate expirationDate) {
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }

    /**
     * Gets how many units are in this batch.
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Updates the quantity for this batch.
     * @param quantity new quantity value
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the expiration date for this batch.
     * @return expiration date
     */
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * Formats the batch info into a readable string.
     * @return something like "Qty: 10, Exp: 2024-12-01"
     */
    @Override
    public String toString() {
        return "Qty: " + quantity + ", Exp: " + expirationDate;
    }
}
