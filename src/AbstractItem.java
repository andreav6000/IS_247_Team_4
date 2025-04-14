import java.time.LocalDate;

/**
 * Abstract class representing a general item in the inventory.
 * This is used as a base class for all types of products/items.
 */
public abstract class AbstractItem {
    // Name of the item
    protected String name;

    // Quantity in stock
    protected int quantity;

    // Expiration date (only applies if perishable is true)
    protected LocalDate expirationDate;

    // Section of the store where this item is located
    protected String section;

    // Indicates if the item is perishable or not
    protected boolean perishable;

    /**
     * Constructor for all inventory items.
     * If the item is perishable, it will store an expiration date.
     * Otherwise, the expiration date is null.
     */
    public AbstractItem(String name, int quantity, LocalDate expirationDate, String section, boolean perishable) {
        this.name = name;
        this.quantity = quantity;
        this.section = section;
        this.perishable = perishable;

        // Only store expiration date if the item is perishable
        if (perishable) {
            this.expirationDate = expirationDate;
        } else {
            this.expirationDate = null;
        }
    }

    // Getter for item name
    public String getName() { return name; }

    // Getter for quantity
    public int getQuantity() { return quantity; }

    // Getter for expiration date
    public LocalDate getExpirationDate() { return expirationDate; }

    // Getter for store section
    public String getSection() { return section; }

    // Check if the item is perishable
    public boolean isPerishable() { return perishable; }

    // Update quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Determines if a perishable item is expired.
     * Non-perishable items are never considered expired.
     */
    public boolean isExpired() {
        if (!perishable) return false;
        return LocalDate.now().isAfter(expirationDate);
    }

    /**
     * Abstract method to return what type of item this is.
     * Must be implemented by subclasses (e.g., Product).
     */
    public abstract String getItemType();

    /**
     * Returns item data in CSV format for file saving.
     * Example: Apple,20,2025-05-01,Vegetables & Fruits,true
     */
    public String toCSV() {
        String expDate = perishable ? expirationDate.toString() : "N/A";
        return name + "," + quantity + "," + expDate + "," + section + "," + perishable;
    }

    /**
     * Basic string representation of the item.
     * Shows important details in human-readable format.
     */
    @Override
    public String toString() {
        String expDate = perishable ? expirationDate.toString() : "N/A";
        return "Name: " + name +
                ", Quantity: " + quantity +
                ", Expires: " + expDate +
                ", Section: " + section +
                ", Type: " + getItemType();
    }
}
