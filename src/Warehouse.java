import java.util.ArrayList;
import java.util.List;

/**
 * A generic warehouse for storing items of a specific type.
 *
 * @param <T> the type of item this warehouse holds; must extend AbstractItem
 *
 * Example:
 * <pre>
 *     Warehouse&lt;Product&gt; warehouse = new Warehouse&lt;&gt;();
 * </pre>
 */
public class Warehouse<T extends AbstractItem> {

    private List<T> items;

    /**
     * Constructs a new Warehouse.
     */
    public Warehouse() {
        items = new ArrayList<>();
    }

    /**
     * Adds an item to the warehouse.
     *
     * @param item the item to add
     */
    public void addItem(T item) {
        items.add(item);
    }

    /**
     * Gets an item by index.
     *
     * @param index the index of the item
     * @return the item at the given index
     */
    public T getItem(int index) {
        return items.get(index);
    }

    /**
     * Gets all items in the warehouse.
     *
     * @return list of all items
     */
    public List<T> getAllItems() {
        return items;
    }
}
