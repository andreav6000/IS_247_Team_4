import java.util.ArrayList;
import java.util.List;

/**
 * Warehouse class that stores a collection of items (products).
 * This is a generic class that can store any type of item that
 * extends AbstractItem.
 *
 * Example usage:
 * Warehouse<Product> warehouse = new Warehouse<>();
 */
public class Warehouse<T extends AbstractItem> {

    // List to store all items in the warehouse
    private List<T> items;

    /**
     * Constructor to create an empty warehouse.
     */
    public Warehouse() {
        items = new ArrayList<>();
    }

    /**
     * Adds an item to the warehouse storage.
     *
     * @param item The item we want to store.
     */
    public void addItem(T item) {
        items.add(item);
    }

    /**
     * Retrieves an item from the warehouse based on its position.
     *
     * @param index The position in the list.
     * @return The item found at that position.
     */
    public T getItem(int index) {
        return items.get(index);
    }

    /**
     * Returns a list of all items stored in the warehouse.
     *
     * @return List of items.
     */
    public List<T> getAllItems() {
        return items;
    }
}