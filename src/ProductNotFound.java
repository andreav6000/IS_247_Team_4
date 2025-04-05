/**
 * Custom exception thrown when a product is not found in the inventory.
 */
public class ProductNotFound extends Exception {
    public ProductNotFound(String message) {
        super(message);
    }
}
