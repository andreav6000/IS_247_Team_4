import java.time.LocalDate;

public abstract class AbstractItem {
    protected String name;
    protected int quantity;
    protected LocalDate expirationDate;
    protected String section;
    protected boolean perishable;
    /**
     * Constructs an item.
     * @param name Name of the item
     * @param quantity Initial quantity
     * @param expirationDate Expiration date (if perishable)
     * @param section Store section
     * @param perishable true if item is perishable
     */

    public AbstractItem(String name, int quantity, LocalDate expirationDate, String section, boolean perishable) {
        this.name = name;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.section = section;
        this.perishable = perishable;
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getSection() { return section; }
    public boolean isPerishable() { return perishable; }
    public LocalDate getExpirationDate() { return expirationDate; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    /**
     * Checks if the item is expired.
     * @return true if expired
     */
    public boolean isExpired() {
        if (!perishable) return false;
        return LocalDate.now().isAfter(expirationDate);
    }
    /**
     * Returns the type of item (defined in subclass).
     * @return item type string
     */
    public abstract String getItemType();
    /**
     * Converts item data into CSV format.
     * @return CSV string
     */
    public String toCSV() {
        String exp = perishable ? expirationDate.toString() : "N/A";
        return name + "," + quantity + "," + exp + "," + section + "," + perishable;
    }

    @Override
    public String toString() {
        String exp = perishable ? expirationDate.toString() : "N/A";
        return "Name: " + name + ", Qty: " + quantity + ", Expires: " + exp + ", Section: " + section;
    }
}