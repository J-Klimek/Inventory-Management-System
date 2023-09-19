package klimek.c482_inventorymanagementsystem;

//javadoc file path is C482_InventoryManagementSystem\javadoc

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import klimek.c482_inventorymanagementsystem.model.InHouse;
import klimek.c482_inventorymanagementsystem.model.Inventory;
import klimek.c482_inventorymanagementsystem.model.OutSourced;
import klimek.c482_inventorymanagementsystem.model.Product;
import java.io.IOException;

/**
 * Main Application Class.
 * This is the main class for the Inventory Management System application.
 *
 *
 * @author James Klimek
 * @version 1.0

 *
 * FUTURE ENHANCEMENT: Add functionality to remove parts from inventory if they are associated
 *                     with a product.
 *
 * FUTURE ENHANCEMENT: Add WARNING Alerts when Inventory is nearing Min or Max
 *                     allowing user to accurately manage inventory with new orders
 *                     or adjusting order quantities.
 */
public class InventoryManagementSystem extends Application {
    /**
     * Start method creates the scene and opens the main Inventory Management System screen.
     * @throws IOException  If an input or output
     *                      exception occurred
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InventoryManagementSystem.class.getResource("MainForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1065, 462);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * This is the main method.
     * This method creates some test data and launches the program.
     */
    public static void main(String[] args) {

        Product Chromebook = new Product(100, "Acer Spin", 550.00, 1357, 50, 1500);
        Product Laptop = new Product(101, "Dell Inspiron 15", 750.00, 100, 5, 100);
        Product Desktop = new Product(102, "Asus Rog Strix", 2500.00, 35, 0, 40);

        Inventory.addProduct(Chromebook);
        Inventory.addProduct(Laptop);
        Inventory.addProduct(Desktop);

        InHouse part1 = new InHouse(1000, "Wired Keyboard", 25.00, 200, 1, 500,103);
        InHouse part2 = new InHouse(1001, "Wired Mouse", 15.00, 200, 1, 500,103);
        OutSourced part3 = new OutSourced(1002, "Wireless Mouse", 25.00, 200, 1, 300,"Logitech");
        OutSourced part4 = new OutSourced(1003, "Wireless Keyboard", 35.00, 200, 1, 200,"Logitech");

        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);
        launch();
    }
}