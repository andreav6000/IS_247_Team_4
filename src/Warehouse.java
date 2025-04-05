import java.util.ArrayList;
import java.util.List;

/**
 * Generic Warehouse class for managing items.
 * @param <T> Type parameter that extends AbstractItem.
 */
public class Warehouse<T extends AbstractItem> {
    private List<T> items;

    public Warehouse() {
        items = new ArrayList<>();
    }

    /**
     * Adds an item to the warehouse.
     * @param item The item to add.
     */
    public void addItem(T item) {
        items.add(item);
    }

    /**
     * Retrieves an item by index.
     * @param index The index of the item.
     * @return The item at the given index.
     */
    public T getItem(int index) {
        return items.get(index);
    }

    /**
     * Gets all items in the warehouse.
     * @return List of items.
     */
    public List<T> getAllItems() {
        return items;
    }
}
