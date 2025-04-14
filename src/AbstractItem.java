import java.time.LocalDate;

/**
 * Abstract class representing an item in inventory.
 * Used as a base class for different product types.
 * Demonstrates abstraction and method overriding.
 */
public abstract class AbstractItem {

    protected String name;
    protected int quantity;
    protected LocalDate expirationDate; // Only for perishable items
    protected String section;
    protected boolean perishable;

    /**
     * Constructor for AbstractItem.
     * @param name The product name.
     * @param quantity Product quantity.
     * @param expirationDate Expiration date if perishable.
     * @param section Store section where product belongs.
     * @param perishable True if the product is perishable.
     */
    public AbstractItem(String name, int quantity, LocalDate expirationDate, String section, boolean perishable) {
        this.name = name;
        this.quantity = quantity;
        this.section = section;
        this.perishable = perishable;
        if (perishable) {
            this.expirationDate = expirationDate;
        } else {
            this.expirationDate = null;
        }
    }

    // Getter methods
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public String getSection() { return section; }
    public boolean isPerishable() { return perishable; }

    /**
     * Sets a new quantity for a non-perishable item.
     * @param quantity New quantity value.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Checks if item is expired.
     * Only works for perishable items.
     * @return True if expired, false otherwise.
     */
    public boolean isExpired() {
        if (!perishable) return false;
        return LocalDate.now().isAfter(expirationDate);
    }

    /**
     * Abstract method to get item type.
     * Implemented by subclasses.
     * @return The type of item.
     */
    public abstract String getItemType();

    /**
     * Returns a CSV representation of the item.
     * @return CSV string of product data.
     */
    public String toCSV() {
        String expDate = perishable ? expirationDate.toString() : "N/A";
        return name + "," + quantity + "," + expDate + "," + section + "," + perishable;
    }

    /**
     * Returns a formatted string to display item info.
     * @return Product details.
     */
    @Override
    public String toString() {
        String expDate = perishable ? expirationDate.toString() : "N/A";
        return "Name: " + name + ", Quantity: " + quantity + ", Expires: " + expDate + ", Section: " + section + ", Type: " + getItemType();
    }
}
