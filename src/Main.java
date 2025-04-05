//Work in progress

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //While statement displays menu options
        while(true) {
            System.out.println("1. Add/Search Product");
            System.out.println("2. Display Inventory");
            System.out.println("3. Check Low Stock");
            System.out.println("4. Check Over Stock");
            System.out.println("5. Check Expired Product");
            System.out.println("6. Exit");

            //Users navigate inventory using numbers
            int choice = scanner.nextInt;
            scanner.nextLine();

            //Switch statements to navigate menu
            switch(choice){
                //Add/Search Product
                case 1:
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextLine();

                    System.out.print("Enter expiration date: ");
                    scanner.nextLine();
                    LocalDate expirationDate = LocalDate.parse(scanner.nextLine(), formatter);

                    System.out.print("Enter location: ");
                    String location = scanner.nextLine();

                    Product newProduct = new Product(name, quantity, expirationDate, location);
                    inventory.addProduct(newProduct);
                    System.out.print("Product is added to cart");
                    break;

                //For the other cases I guess we can use different files
                //that each serve a function (i.e. display inventory, check low stock etc.)
            }
        }
    }
}