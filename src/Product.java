import java.time.LocalDate;

/**
 * Class representing a product in the inventory.
 * Inherits from AbstractItem and implements StockAdjustable.
 */
public class Product extends AbstractItem implements StockAdjustable {

    /**
     * Constructs a Product.
     * @param name Name of the product.
     * @param quantity Initial quantity.
     * @param expirationDate Expiration date (if perishable; otherwise null).
     * @param section Store section.
     * @param perishable true if the product is perishable.
     */
    public Product(String name, int quantity, LocalDate expirationDate, String section, boolean perishable) {
        super(name, quantity, expirationDate, section, perishable);
    }

    @Override
    public String getItemType() {
        return "Product";
    }

    @Override
    public void addStock(int amount) {
        this.quantity += amount;
    }

    @Override
    public void removeStock(int amount) throws IllegalArgumentException {
        if(amount > this.quantity) {
            throw new IllegalArgumentException("Not enough stock to remove.");
        }
        this.quantity -= amount;
    }

    /**
     * Updates stock using a new quantity.
     * @param newQuantity the new quantity.
     */
    public void updateStock(int newQuantity) {
        this.quantity = newQuantity;
    }

    /**
     * Updates stock using an operator.
     * @param operator "+" to add, "-" to subtract.
     * @param value value to update.
     */
    public void updateStock(String operator, int value) {
        if(operator.equals("+")) {
            this.quantity += value;
        } else if(operator.equals("-")) {
            this.quantity -= value;
        }
    }
}
