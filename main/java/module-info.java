module klimek.c482_inventorymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens klimek.c482_inventorymanagementsystem to javafx.fxml;
    exports klimek.c482_inventorymanagementsystem;
    exports klimek.c482_inventorymanagementsystem.model;
    opens klimek.c482_inventorymanagementsystem.model to javafx.fxml;

}