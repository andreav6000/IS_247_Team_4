import java.util.List;
import java.util.Map;

/**
 * This class is used to display product categories on the screen.
 * It mimics a touchscreen interface (like the ones used in self-checkout or stock apps).
 */
public class DisplayUI {

    /**
     * Shows a grid of categories with how many items are in each one.
     * This makes it easy for the user to quickly see where products are.
     *
     * @param categoryMap a map where the key is the category name and the value is the list of products in that category
     */
    public static void displayCategorySummary(Map<String, List<Product>> categoryMap) {
        System.out.println("===== Select a Category =====");
        int colCount = 0;

        for (Map.Entry<String, List<Product>> entry : categoryMap.entrySet()) {
            String category = entry.getKey();      // Example: "Snacks"
            int count = entry.getValue().size();   // How many products are in that category

            // Display category name and number of products, aligned in columns
            System.out.printf("[%-18s] %3d Foods\t", category, count);
            colCount++;

            // Break the line after every 3 categories to make it look like a grid
            if (colCount % 3 == 0) System.out.println();
        }

        // If it didn’t print a full last row, just move to the next line
        if (colCount % 3 != 0) System.out.println();
    }
}
