// Possible changes for this file

import java.time.LocalDate;

public class Product {
    private String name;
    private int quantity;
    private LocalDate expirationDate;
    private String location;

    //Constructors for the product
    public Product(String name, int quantity, LocalDate expirationDate, String location) {
        this.name = name;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.location = location;
    }
    //Getter methods for name, quantity, dates, and its location
    public String getName() {
        return name;
    }
    public int getQuantity() {
        return quantity;
    }
    public LocalDate getExpirationDate() {
        return expirationDate;
    }
    public String getLocation() {
        return location;
    }
    //Setter to update quantity of a product
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    //Method to check the expiration date's label on the product
    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }
    //Overrides the toString to represent the current product
    @Override
    public String toString() {
        return name + "Quantity: " + quantity + "Expiration Date: " + expirationDate + "Location: " + location;
    }
}
