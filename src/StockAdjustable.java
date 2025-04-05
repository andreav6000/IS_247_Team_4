/**
 * Interface for stock adjustments.
 */
public interface StockAdjustable {
    /**
     * Adds stock to an item.
     * @param amount the amount to add.
     */
    void addStock(int amount);

    /**
     * Removes stock from an item.
     * @param amount the amount to remove.
     * @throws IllegalArgumentException if there is insufficient stock.
     */
    void removeStock(int amount) throws IllegalArgumentException;
}
