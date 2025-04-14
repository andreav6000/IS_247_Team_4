/**
 * Interface that defines basic operations for updating stock.
 * Used by items that allow stock to be added or removed.
 */
public interface StockAdjustable {

    /**
     * Add stock to the item.
     *
     * @param amount the amount to add
     */
    void addStock(int amount);

    /**
     * Remove stock from the item.
     *
     * @param amount the amount to remove
     * @throws IllegalArgumentException if trying to remove more than available
     */
    void removeStock(int amount) throws IllegalArgumentException;
}
