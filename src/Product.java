import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a product in the inventory.
 * Inherits from AbstractItem and implements StockAdjustable.
 * Now supports multiple batches with different expiration dates.
 */
public class Product extends AbstractItem implements StockAdjustable {

    private List<ProductBatch> batches;

    /**
     * Constructs a Product with initial quantity, expiration, section, and perishable status.
     */
    public Product(String name, int quantity, LocalDate expirationDate, String section, boolean perishable) {
        super(name, 0, null, section, perishable);
        this.batches = new ArrayList<>();
        if (perishable) {
            addOrUpdateBatch(quantity, expirationDate);
        } else {
            this.quantity = quantity; // for non-perishable
        }
    }

    /**
     * Adds a batch or updates an existing one with the same expiration date.
     */
    public void addOrUpdateBatch(int qty, LocalDate expirationDate) {
        for (ProductBatch batch : batches) {
            if (batch.getExpirationDate().equals(expirationDate)) {
                batch.setQuantity(batch.getQuantity() + qty);
                return;
            }
        }
        batches.add(new ProductBatch(qty, expirationDate));
    }

    @Override
    public int getQuantity() {
        if (!perishable) return quantity;
        return batches.stream().mapToInt(ProductBatch::getQuantity).sum();
    }

    @Override
    public LocalDate getExpirationDate() {
        if (!perishable) return null;
        return batches.stream()
                .map(ProductBatch::getExpirationDate)
                .min(LocalDate::compareTo)
                .orElse(null);
    }

    public List<ProductBatch> getBatches() {
        return batches;
    }

    @Override
    public String getItemType() {
        return "Product";
    }

    @Override
    public void addStock(int amount) {
        if (!perishable) {
            this.quantity += amount;
        }
    }

    @Override
    public void removeStock(int amount) throws IllegalArgumentException {
        if (!perishable) {
            if (amount > this.quantity) throw new IllegalArgumentException("Not enough stock");
            this.quantity -= amount;
        }
    }

    public void updateStock(int newQuantity) {
        if (!perishable) {
            this.quantity = newQuantity;
        }
    }

    public void updateStock(String operator, int value) {
        if (!perishable) {
            if (operator.equals("+")) this.quantity += value;
            else if (operator.equals("-")) this.quantity -= value;
        }
    }

    @Override
    public boolean isExpired() {
        if (!perishable) return false;
        LocalDate today = LocalDate.now();
        return batches.stream().allMatch(batch -> today.isAfter(batch.getExpirationDate()));
    }

    @Override
    public String toString() {
        if (!perishable) {
            return "Name: " + name + ", Quantity: " + quantity + ", Section: " + section + ", Type: Product";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Name: ").append(name).append(", Section: ").append(section).append(", Batches: [");
            for (ProductBatch batch : batches) {
                sb.append(batch.toString()).append(", ");
            }
            if (!batches.isEmpty()) sb.setLength(sb.length() - 2);
            sb.append("]");
            return sb.toString();
        }
    }

    @Override
    public String toCSV() {
        if (!perishable) {
            return name + "," + quantity + ",N/A," + section + ",false";
        } else {
            ProductBatch first = batches.get(0);
            return name + "," + first.getQuantity() + "," + first.getExpirationDate() + "," + section + ",true";
        }
    }
}