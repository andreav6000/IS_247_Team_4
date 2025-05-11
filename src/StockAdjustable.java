/**
 * Interface for adjusting stock.
 * Demonstrates interface implementation.
 */
public interface StockAdjustable {

    /**
     * Adds stock to the item.
     * @param amount Amount to add.
     */
    void addStock(int amount);

    /**
     * Removes stock from the item.
     * @param amount Amount to remove.
     */
    void removeStock(int amount) throws IllegalArgumentException;
}