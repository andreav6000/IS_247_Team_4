import java.time.LocalDate;

public class ProductBatch {
    private int quantity;
    private LocalDate expirationDate;

    public ProductBatch(int quantity, LocalDate expirationDate) {
        this.quantity = quantity;
        this.expirationDate = expirationDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Override
    public String toString() {
        return "Qty: " + quantity + ", Exp: " + expirationDate;
    }
}
