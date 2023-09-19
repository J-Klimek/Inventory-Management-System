package klimek.c482_inventorymanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import klimek.c482_inventorymanagementsystem.model.InHouse;
import klimek.c482_inventorymanagementsystem.model.Inventory;
import klimek.c482_inventorymanagementsystem.model.OutSourced;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for adding Parts.
 */
public class AddPartController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private Label addPartLbl;
    @FXML
    private RadioButton inHouseBtn;
    @FXML
    private RadioButton outsourcedBtn;
    @FXML
    private TextField partCostTxt;
    @FXML
    private Label partCurrInventoryLbl;
    @FXML
    private TextField partCurrInventoryTxt;
    @FXML
    private Label partIDLbl;
    @FXML
    private TextField partIDTxt;
    @FXML
    private Label partInvMaxLbl;
    @FXML
    private TextField partInvMaxTxt;
    @FXML
    private Label partInvMinLbl;
    @FXML
    private TextField partInvMinTxt;
    @FXML
    private Label partNameLbl;
    @FXML
    private TextField partNameTxt;
    @FXML
    private Label partPriceLbl;
    @FXML
    private Label partMachineIDLbl;
    @FXML
    private TextField partMachineIDTxt;
    private static int nextPartID = 1000;
    @FXML
    void onActionAddPartCancel(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    @FXML
    void onActionAddPartSave(ActionEvent event) throws IOException {
        int id = nextPartID;
        double price = 0.00;
        int stock = 0;
        int min = 0;
        int max = 0;
        String name = partNameTxt.getText();
        //Try setting all fields and throw respective errors with alerts on exceptions
        try {
            InventoryController.error = "Price";
            price = Double.parseDouble(partCostTxt.getText());
            InventoryController.error = "Inventory";
            stock = Integer.parseInt(partCurrInventoryTxt.getText());
            InventoryController.error = "Min";
            min = Integer.parseInt(partInvMinTxt.getText());
            InventoryController.error = "Max";
            max = Integer.parseInt(partInvMaxTxt.getText());

        partIDTxt.setText(String.valueOf(id));
        //input validation checks
        while(Inventory.lookupPart(id) != null){
            AddPartController.nextPartID++;
            id = nextPartID;
        }
        if(name.isBlank()){
            InventoryController.error = "Name";
            InventoryController.invalidNameAlert();
            return;
        }
        if(stock < min || stock > max){
            InventoryController.invalidStockAlert();
            return;
        }
        if(price < 0.00){
            InventoryController.invalidPriceAlert();
            return;
        }

        if(inHouseBtn.isSelected()){
            InventoryController.error = "Machine ID";
            int machineID = Integer.parseInt(partMachineIDTxt.getText());
            InventoryController.confirmSaveAlert();
            Optional<ButtonType> result = InventoryController.confirmSave.showAndWait();
            if (result.get() == ButtonType.OK) {
                InHouse inHousePart = new InHouse(id, name, price, stock, min, max, machineID);
                Inventory.addPart(inHousePart);
            }
            else{
                return;
            }
        }
        else if(outsourcedBtn.isSelected()) {
            String companyName = partMachineIDTxt.getText();
            if (companyName.isBlank()) {
                InventoryController.error = "Company Name";
                InventoryController.invalidNameAlert();
                return;
            }
            InventoryController.confirmSaveAlert();
            Optional<ButtonType> result = InventoryController.confirmSave.showAndWait();
            if (result.get() == ButtonType.OK) {
                OutSourced outsourcedPart = new OutSourced(id, name, price, stock, min, max, companyName);
                Inventory.addPart(outsourcedPart);
            }
            else {
                return;
            }
        }
        }
        catch(NumberFormatException numEx){
            InventoryController.invalidNumberAlert();
            return;
        }

        AddPartController.nextPartID++;

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Inventory Management System");
        stage.show();
    }

    @FXML
    void onActionSelectInHouse(ActionEvent event) {
        partMachineIDLbl.setText("Machine ID");
    }

    @FXML
    void onActionSelectOutsourced(ActionEvent event) {
        partMachineIDLbl.setText("Company Name");
    }

    /**
     * Does nothing.
     */
    @Override
    public void initialize(URL rl, ResourceBundle rb){
    }
}
