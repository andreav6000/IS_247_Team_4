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
                    System.out.print("Enter product name");
                    String name = scanner.nextLine();
                    break;

                //For the other cases I guess we can use different files
                //that each serve a function (i.e. display inventory, check low stock etc.)
            }
        }
    }
}