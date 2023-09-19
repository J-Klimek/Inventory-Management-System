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
import klimek.c482_inventorymanagementsystem.model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for modifying parts.
 * Controls the Modify Part Screen in the program.
 */
public class ModifyPartController implements Initializable{
    private Part partToModify;
    Stage stage;
    Parent scene;
    @FXML
    private RadioButton inHouseBtn;

    @FXML
    private Label modifyPartLbl;

    @FXML
    private RadioButton outsourcedBtn;

    @FXML
    private Label partCostLbl;

    @FXML
    private TextField partCostTxt;

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
    private Label partInventoryLbl;

    @FXML
    private Label partNameLbl;

    @FXML
    private TextField partNameTxt;

    @FXML
    private Label partMachineIDLbl;

    @FXML
    private TextField partMachineIDTxt;

    @FXML
    void OnActionSelectInHouse(ActionEvent event) {
        partMachineIDLbl.setText("Machine ID");
    }
    @FXML
    void onActionSelectOutsourced(ActionEvent event) {
        partMachineIDLbl.setText("Company Name");
    }

    @FXML
    void onActionCancelPartModify(ActionEvent event) throws IOException {
        InventoryController.confirmCancelAlert();
        Optional<ButtonType> result = InventoryController.confirmCancel.showAndWait();
        if (result.get() == ButtonType.OK){
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    @FXML
    void onActionSavePartModify(ActionEvent event) throws IOException {
        int id = Integer.parseInt(partIDTxt.getText());
        String name = partNameTxt.getText();

        try {
            InventoryController.error = "Price";
            double price = Double.parseDouble(partCostTxt.getText());
            InventoryController.error = "Inventory";
            int stock = Integer.parseInt(partCurrInventoryTxt.getText());
            InventoryController.error = "Min";
            int min = Integer.parseInt(partInvMinTxt.getText());
            InventoryController.error = "Max";
            int max = Integer.parseInt(partInvMaxTxt.getText());

            partIDTxt.setText(String.valueOf(id));

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
                InventoryController.error = "MachineID";
                int machineID = Integer.parseInt(partMachineIDTxt.getText());
                InventoryController.confirmSaveAlert();
                Optional<ButtonType> result = InventoryController.confirmSave.showAndWait();
                if (result.get() == ButtonType.OK) {
                    InHouse updatedPart = new InHouse(id, name, price, stock, min, max, machineID);
                    Inventory.updatePart(Inventory.getAllParts().indexOf(partToModify), updatedPart);
                }
                else{
                    return;
                }
            }
            else if(outsourcedBtn.isSelected()){
                String companyName = partMachineIDTxt.getText();
                if(companyName.isBlank()){
                    InventoryController.invalidNameAlert();
                    return;
                }
                InventoryController.confirmSaveAlert();
                Optional<ButtonType> result = InventoryController.confirmSave.showAndWait();
                if (result.get() == ButtonType.OK) {
                    OutSourced updatedPart = new OutSourced(id, name, price, stock, min, max, companyName);
                    Inventory.updatePart(Inventory.getAllParts().indexOf(partToModify), updatedPart);
                }
                else{
                    return;
                }
            }
        }
        catch(NumberFormatException numEx){
            InventoryController.invalidNumberAlert();
            return;
        }
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Initializes the ModifyPartController Class.
     * Creates a Part object and sets it to the selected Part from the Inventory Controller.
     * Populates the text fields with the selected Part's attributes.
     */
    @Override
    public void initialize(URL rl, ResourceBundle rb){

        partToModify = InventoryController.getSelectedPart();

        String id = String.valueOf(partToModify.getId());
        String price = String.valueOf(partToModify.getPrice());
        String stock = String.valueOf(partToModify.getStock());
        String min = String.valueOf(partToModify.getMin());
        String max = String.valueOf(partToModify.getMax());
        String name = partToModify.getName();
        String sourceIdentifier = "";

        if(partToModify instanceof InHouse) {
            inHouseBtn.setSelected(true);
            OnActionSelectInHouse(new ActionEvent());
            sourceIdentifier = String.valueOf(((InHouse) partToModify).getMachineID());
        }
        if(partToModify instanceof OutSourced){
            outsourcedBtn.setSelected(true);
            onActionSelectOutsourced(new ActionEvent());
            sourceIdentifier = ((OutSourced) partToModify).getCompanyName();
        }

        partIDTxt.setText(id);
        partCostTxt.setText(price);
        partCurrInventoryTxt.setText(stock);
        partInvMinTxt.setText(min);
        partInvMaxTxt.setText(max);
        partNameTxt.setText(name);
        partMachineIDTxt.setText(sourceIdentifier);

    }

}
