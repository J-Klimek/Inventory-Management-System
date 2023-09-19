package klimek.c482_inventorymanagementsystem;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import klimek.c482_inventorymanagementsystem.model.Inventory;
import klimek.c482_inventorymanagementsystem.model.Part;
import klimek.c482_inventorymanagementsystem.model.Product;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for InventoryManagementSystem(main).
 * Initializes the Part and Product TableViews with All added Part and Product Objects.
 */
public class InventoryController implements Initializable {
    Stage stage;
    Parent scene;

    //static attributes and methods to send selected row from part or product tableviews to the Modify Part or Product Screens
    private static Part selectedPart = null;
    private static Product selectedProduct = new Product(0,"no name",0.00, 1, 0, 2);
    public static String error = "";

    /**
     * Gets selected Part from Part Tableview for modify and delete methods.
     * @return selectedPart
     */
    public static Part getSelectedPart(){
        return selectedPart;
    }

    /**
     * Gets selected Product from Product Tableview for modify and delete methods.
     * @return selectedProduct
     */
    public static Product getSelectedProduct() {
        return selectedProduct;
    }

    //static attributes and methods for Alert pop-ups
    public static Alert invalidStock = new Alert(Alert.AlertType.ERROR);
    public static Alert invalidName = new Alert(Alert.AlertType.ERROR);
    public static Alert invalidNumber = new Alert(Alert.AlertType.ERROR);
    public static Alert invalidPrice = new Alert(Alert.AlertType.ERROR);
    public static Alert invalidSelection = new Alert(Alert.AlertType.ERROR);
    public static Alert productHasAssocPartsAlert = new Alert(Alert.AlertType.ERROR);
    public static Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
    public static Alert confirmSave = new Alert(Alert.AlertType.CONFIRMATION);
    public static Alert confirmCancel = new Alert(Alert.AlertType.CONFIRMATION);
    public static Alert confirmExit = new Alert(Alert.AlertType.CONFIRMATION);

    /**
     * Static method implementing the Invalid Inventory message ERROR.
     * Inventory must be between minimum inventory and maximum inventory.
     */
    public static void invalidStockAlert(){
        invalidStock.setTitle("!! Invalid Inventory !!");
        invalidStock.setContentText("Inventory must be higher than min and lower than max");
        invalidStock.show();
    }

    /**
     * Static method implementing the Invalid Name message ERROR.
     * Name cannot be blank.
     */
    public static void invalidNameAlert(){
        invalidName.setTitle("!! Invalid Name !!");
        invalidName.setContentText("" + error + " cannot be blank!");
        invalidName.show();
    }

    /**
     * Static method implementing the Invalid Number message ERROR.
     * Input must be a number.
     */
    public static void invalidNumberAlert(){
        invalidNumber.setTitle("!! Invalid Number !!");
        invalidNumber.setContentText("" + error + " needs to be a number!");
        invalidNumber.show();
    }

    /**
     * Static method implementing the Invalid Price message ERROR.
     * Price must be a positive number.
     */
    public static void invalidPriceAlert(){
        invalidPrice.setTitle("!! Invalid Price !!");
        invalidPrice.setContentText("Price must be a positive number");
        invalidPrice.show();
    }

    /**
     * Static method implementing the Invalid Selection message ERROR.
     * Alerts when user has not selected anything when required for a method call.
     */
    public static void invalidSelectionAlert(){
        invalidSelection.setTitle("!! Invalid Selection !!");
        invalidSelection.setContentText("Nothing was selected!");
        invalidSelection.show();
    }

    /**
     * Static method implementing the Has associated Parts message ERROR.
     * Alerts when a user attempts to delete a Product that has associated parts.
     */
    public static void productHasAssocPartsAlert(){
        productHasAssocPartsAlert.setTitle(("!! Unable to DELETE " + InventoryController.getSelectedProduct().getName() + " !!"));
        productHasAssocPartsAlert.setContentText(InventoryController.getSelectedProduct().getName() + " has parts associated with it.\nTo delete, modify product and disassociate all parts.");
        productHasAssocPartsAlert.show();
    }

    /**
     * Static method to pop up a confirmation window when attempting to delete.
     * Asks the user to confirm delete.
     */
    public static void confirmDeleteAlert(){
        confirmDelete.setTitle("DELETE Confirmation");
        confirmDelete.setContentText("Are you sure you want to DELETE?");
    }
    /**
     * Static method to pop up a confirmation window when attempting to Save.
     * Asks the user to confirm save.
     */
    public static void confirmSaveAlert(){
        confirmSave.setTitle("SAVE Confirmation");
        confirmSave.setContentText("Are you sure you want to SAVE?");
    }
    /**
     * Static method to pop up a confirmation window when attempting to Cancel a process.
     * Asks the user to confirm cancel.
     */
    public static void confirmCancelAlert(){
        confirmCancel.setTitle("CANCEL Confirmation");
        confirmCancel.setContentText("Are you sure you want to CANCEL?");
    }
    /**
     * Static method to pop up a confirmation window when attempting to close the program.
     * Asks the user to confirm closing the program.
     */
    public static void confirmExitAlert(){
        confirmExit.setTitle("Exit Program");
        confirmExit.setContentText("Would you like to close the program?");
    }


    @FXML
    private Label InvMgmtSysLbl;

    @FXML
    private AnchorPane MainForm;

    @FXML
    private TableColumn<Part, Integer> partIDCol;

    @FXML
    private TableColumn<Part, Integer> partInvCol;

    @FXML
    private Label partLbl;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TextField partSearchTxt;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableColumn<Product, Integer> productIDCol;

    @FXML
    private TableColumn<Product, Integer> productInvCol;

    @FXML
    private Label productLbl;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Double> productPriceCol;

    @FXML
    private TextField productSearchTxt;
    @FXML
    private TableView<Product> productTableView;


    @FXML
    void OnActionSearchPart(ActionEvent event){
        String searchQuery = partSearchTxt.getText();
        ObservableList<Part> parts = Inventory.lookupPart(searchQuery);
        if (parts.size() == 0){
            try {
                int id = Integer.parseInt(searchQuery);
                Part part = Inventory.lookupPart(id);
                if (part != null) {
                    parts.add(part);
                }
            }
            catch (NumberFormatException ex1){
                //ignore
            }
        }
        partTableView.setItems(parts);
    }
    @FXML
    void OnActionSearchProduct(ActionEvent event){
        String searchQuery = productSearchTxt.getText();

        ObservableList<Product> products = Inventory.lookupProduct(searchQuery);
        if (products.size() == 0){
            try {
                int id = Integer.parseInt(searchQuery);
                Product product = Inventory.lookupProduct(id);
                if (product != null) {
                    products.add(product);
                }
            }
            catch (NumberFormatException e){
                //ignore
            }
        }

        productTableView.setItems(products);
    }
    /**
     * @throws IOException  If an input or output
     *                      exception occurred
     */
    @FXML
    void OnActionOpenAddPart(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AddPartForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.setTitle("Add Part");
        stage.show();
    }
    @FXML
    void OnActionOpenModifyPart(ActionEvent event) {
        selectedPart = partTableView.getSelectionModel().getSelectedItem();
        try {
            scene = FXMLLoader.load(getClass().getResource("ModifyPartForm.fxml"));
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            stage.setTitle("Modify Part");
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(Exception e){
            invalidSelectionAlert();
        }
    }
    @FXML
    void OnActionDeletePart(){
        boolean delPart = false;
        selectedPart = partTableView.getSelectionModel().getSelectedItem();
        delPart = Inventory.deletePart(selectedPart);
        if (delPart) {
            confirmDeleteAlert();
            Optional<ButtonType> result = confirmDelete.showAndWait();
            if (result.get() == ButtonType.OK){
                Inventory.getAllParts().remove(selectedPart);
            }
        }
    }
    @FXML
    void OnActionOpenAddProduct(ActionEvent event) {
        try {
            scene = FXMLLoader.load(getClass().getResource("AddProductForm.fxml"));
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(scene));
            stage.setTitle("Add Product");
            stage.show();
        }
        catch(Exception e){
            //ignore
        }
    }
    @FXML
    void OnActionOpenModifyProduct(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ModifyProductForm.fxml"));
            Parent modifyProductScene = loader.load();
            Scene scene = new Scene(modifyProductScene);
            ModifyProductController mpc = loader.getController();
            mpc.sendProduct(productTableView.getSelectionModel().getSelectedItem());
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Modify Product");
            stage.show();
        }
        catch(Exception e){
            InventoryController.invalidSelectionAlert();;
        }
    }
    @FXML
    void OnActionDeleteProduct(ActionEvent event) {
        boolean delProduct = false;
        selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        delProduct = Inventory.deleteProduct(selectedProduct);
        if (delProduct) {
            confirmDeleteAlert();
            Optional<ButtonType> result = confirmDelete.showAndWait();
            if (result.get() == ButtonType.OK){
                Inventory.getAllProducts().remove(selectedProduct);
            }
        }
    }
    @FXML
    void OnActionExit(ActionEvent event) {
        InventoryController.confirmExitAlert();
        Optional<ButtonType> result = InventoryController.confirmExit.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * Initializes the Inventory Controller.
     * Sets Tableview column names and populates the Part and Product Tableviews.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partTableView.setItems(Inventory.getAllParts());
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTableView.setItems(Inventory.getAllProducts());
        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}