//Possible changes for this file

import java.util.ArrayList;
import java.util.List;

//Will keep track/manage an inventory of product/items
public class Inventory {
    //Private list that stores the products inside the inventory
    private List<Product> products = new ArrayList<>();

    //Constants for low stocks and overstocks. Will notify stock inventory amount
    private static final int LOW_STOCK = 5;
    private static final int OVER_STOCK = 100;

    //Add products to the list, which is the inventory
    public void addProduct(Product product) {
        products.add(product);
    }

    //Update the stock quantity for the named product
    public void updateStock(String productName, int newQuantity) {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(productName)) { // Case insensitive match
                //Updates the quantity of the named product and once successful,
                //the return will exit when the product is found and will notify its update
                product.setQuantity(newQuantity);
                System.out.print(productName + "'s stock has been updated");
                return;
            }
        }
        //In case the product can not be found, it will leave this message
        System.out.print("Product can not be found");
    }
    //Simply checks for a product on low stock
    public void checkLowStock() {
        for (Product product: products) {
            if (product.getQuantity() < LOW_STOCK) {
                System.out.println("Low stock: " + product);
            }
        }
    }
    //Checks if overstock of product
    public void checkOverStock() {
        for (Product product : products) {
            if (product.getQuantity() > OVER_STOCK) {
                System.out.println("Over stock: " + product);
            }
        }
    }
    //Check for expiration date of a product
    public void checkExpiredProducts() {
        for (Product product : products) {
            if (product.isExpired()) {
                System.out.println("Expired: " + product);
            }
        }
    }
    //Display products in the list/inventory
    public void displayInventory() {
        for (Product product : products) {
            System.out.println(product);
        }
    }
}
