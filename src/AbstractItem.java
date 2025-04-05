import java.time.LocalDate;

/**
 * Abstract class representing an item in inventory.
 * It includes fields for name, quantity, expiration date (if perishable), store section,
 * and a flag indicating if the item is perishable.
 */
public abstract class AbstractItem {
    protected String name;
    protected int quantity;
    protected LocalDate expirationDate; // Only meaningful if perishable is true.
    protected String section;
    protected boolean perishable;

    /**
     * Constructor for an inventory item.
     * @param name Name of the item.
     * @param quantity Current quantity.
     * @param expirationDate Expiration date (if perishable).
     * @param section Store section (e.g., "Vegetables & Fruits", "Electronics", etc.).
     * @param perishable true if the item is perishable.
     */
    public AbstractItem(String name, int quantity, LocalDate expirationDate, String section, boolean perishable) {
        this.name = name;
        this.quantity = quantity;
        this.section = section;
        this.perishable = perishable;
        if(perishable) {
            this.expirationDate = expirationDate;
        } else {
            this.expirationDate = null;
        }
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public String getSection() { return section; }
    public boolean isPerishable() { return perishable; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Checks if the item is expired.
     * For non-perishable items, this always returns false.
     * @return true if expired.
     */
    public boolean isExpired() {
        if (!perishable) return false;
        return LocalDate.now().isAfter(expirationDate);
    }

    /**
     * Abstract method to get the type of item.
     * @return the item type as a String.
     */
    public abstract String getItemType();

    /**
     * Returns a CSV representation of the item.
     * Format: name,quantity,expirationDate,section,perishable.
     * For non-perishable items, expirationDate is represented as "N/A".
     * @return CSV-formatted string.
     */
    public String toCSV() {
        String expDate = perishable ? expirationDate.toString() : "N/A";
        return name + "," + quantity + "," + expDate + "," + section + "," + perishable;
    }

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
