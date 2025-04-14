/**
 * Custom exception thrown when a product is not found in the inventory.
 * Helps handle errors during product search/update more clearly.
 */
public class ProductNotFound extends Exception {

    /**
     * Passes the error message to the superclass Exception.
     *
     * @param message Custom error message describing the issue.
     */
    public ProductNotFound(String message) {
        super(message);
    }
}
