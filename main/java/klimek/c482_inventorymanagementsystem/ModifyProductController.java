package klimek.c482_inventorymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import klimek.c482_inventorymanagementsystem.model.Inventory;
import klimek.c482_inventorymanagementsystem.model.Part;
import klimek.c482_inventorymanagementsystem.model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for modifying Products.
 */
public class ModifyProductController implements Initializable{
    Stage stage;
    Parent scene;
    @FXML
    private Label modifyProductLbl;
    @FXML
    private TableView<Part> modProdPartsTableView;

    @FXML
    private TableView<Part> modProductTopTableView;

    @FXML
    private TableColumn<Part, Double> partAssocCostCol;

    @FXML
    private TableColumn<Part, Integer> partAssocIDCol;

    @FXML
    private TableColumn<Part, Integer> partAssocInventoryCol;

    @FXML
    private TableColumn<Part, String> partAssocNameCol;

    @FXML
    private TableColumn<Part, Double> partCostCol;

    @FXML
    private TableColumn<Part, Integer> partIDCol;

    @FXML
    private TableColumn<Part, Integer> partInventoryCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private Label productIDLbl;

    @FXML
    private TextField productIDTxt;

    @FXML
    private Label productInvMaxLbl;

    @FXML
    private TextField productInvMaxTxt;

    @FXML
    private Label productInvMinLbl;

    @FXML
    private TextField productInvMinTxt;

    @FXML
    private Label productInventoryLbl;

    @FXML
    private TextField productInventoryTxt;

    @FXML
    private Label productNameLbl;

    @FXML
    private TextField productNameTxt;

    @FXML
    private Label productPriceLbl;

    @FXML
    private TextField productPriceTxt;

    @FXML
    private TextField searchPartTxt;

    //product object used to update the product object in the allProducts list
    Product updatedProduct = new Product(0,"no name",0.00,0,0,0);
    // Local static Product object used in the passProduct method
    private static Product productToModify = null;
    // Local static ObservableList to store parts associated with a specific product
    private static ObservableList<Part> productParts = null;

    /**
     * Method to pass the selectedPart information from AddProductController to ModifyProductController.
     * @param selectedProduct Current selected product in the Products list.
     *
     *  <b>RUNTIME ERROR:</b>   I ran into a bug where my Modify Product Screen was not
     *                          populating the associated parts list.
     *                          I found the error in my code, where I incorrectly used the
     *                          Column Labels instead of the TextFields.
     */
    public void sendProduct(Product selectedProduct) {
        productToModify = selectedProduct;
        productParts = FXCollections.observableArrayList(productToModify.getAllAssociatedParts());
        productIDTxt.setText(Integer.toString(productToModify.getId()));
        productNameTxt.setText(productToModify.getName());
        productInventoryTxt.setText(Integer.toString(productToModify.getStock()));
        productPriceTxt.setText(Double.toString(productToModify.getPrice()));
        productInvMinTxt.setText(Integer.toString(productToModify.getMin()));
        productInvMaxTxt.setText(Integer.toString(productToModify.getMax()));
        modProdPartsTableView.setItems(productParts);

        for (int i = 0; i < modProdPartsTableView.getItems().size(); i++) {
            updatedProduct.addAssociatedPart(modProdPartsTableView.getItems().get(i));
        }
    }

    @FXML
    void OnActionSearchPart(ActionEvent event){
        String searchQuery = searchPartTxt.getText();
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
        modProductTopTableView.setItems(parts);
    }
    @FXML
    void onActionModifyPartAssocAdd(ActionEvent event) {
        Part partToAdd = modProductTopTableView.getSelectionModel().getSelectedItem();
        updatedProduct.addAssociatedPart(partToAdd);
        modProdPartsTableView.setItems(updatedProduct.getAllAssociatedParts());
    }
    @FXML
    void onActionModifyPartAssocRemove(ActionEvent event) {
        productParts.remove(modProdPartsTableView.getSelectionModel().getSelectedItem());
        productToModify.deleteAssociatedPart(modProductTopTableView.getSelectionModel().getSelectedItem());
    }
    @FXML
    void onActionModifyProductCancel(ActionEvent event) throws IOException {
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
    void onActionModifyProductSave(ActionEvent event) throws IOException {
        int prodId = Integer.parseInt(productIDTxt.getText());
        String prodName = productNameTxt.getText();
        double prodPrice;
        int prodStock;
        int prodStockMin;
        int prodStockMax;

        try {
            InventoryController.error = "Price";
            prodPrice = Double.parseDouble(productPriceTxt.getText());
            InventoryController.error = "Inventory";
            prodStock = Integer.parseInt(productInventoryTxt.getText());
            InventoryController.error = "Min";
            prodStockMin = Integer.parseInt(productInvMinTxt.getText());
            InventoryController.error = "Max";
            prodStockMax = Integer.parseInt(productInvMaxTxt.getText());

            productIDTxt.setText(String.valueOf(prodId));

            if (prodName.isBlank()) {
                InventoryController.error = "Product Name";
                InventoryController.invalidNameAlert();
                return;
            }
            if (prodStock < prodStockMin || prodStock > prodStockMax) {
                InventoryController.invalidStockAlert();
                return;
            }
            if (prodPrice < 0.00) {
                InventoryController.invalidPriceAlert();
                return;
            }
        }
        catch(NumberFormatException numEx){
            InventoryController.invalidNumberAlert();
            return;
        }

        InventoryController.confirmSaveAlert();
        Optional<ButtonType> result = InventoryController.confirmSave.showAndWait();

        if (result.get() == ButtonType.OK){
            Product updatedProduct = new Product(prodId, prodName, prodPrice, prodStock, prodStockMin, prodStockMax);
            for(int i = 0; i < modProdPartsTableView.getItems().size(); i++){
                Part partToAdd = modProdPartsTableView.getItems().get(i);
                updatedProduct.addAssociatedPart(partToAdd);
            }
            Inventory.updateProduct(Inventory.getAllProducts().indexOf(productToModify),updatedProduct);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("Inventory Management System");
            stage.show();
        }
    }

        /**
         * Initializes the AddPartController Class.
         * Sets the top tableview and populates with all parts.
         * Sets the bottom tableview for the selected Products associated parts.
         */
        @Override
    public void initialize (URL rl, ResourceBundle rb){
            modProductTopTableView.setItems(Inventory.getAllParts());
            partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            partCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

            partAssocIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            partAssocNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            partAssocInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
            partAssocCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
