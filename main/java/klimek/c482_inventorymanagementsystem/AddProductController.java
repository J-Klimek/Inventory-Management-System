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
import javafx.stage.Stage;
import klimek.c482_inventorymanagementsystem.model.Inventory;
import klimek.c482_inventorymanagementsystem.model.Part;
import klimek.c482_inventorymanagementsystem.model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Controller Class that creates a new Product Object and adds it to the Inventory Models allProducts list.
 */
public class AddProductController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private Label addProductLbl;

    @FXML
    private TableView<Part> addProdPartAssocTableView;

    @FXML
    private TableView<Part> addProductTopTableView;

    @FXML
    private TableColumn<Part, Double> allPartsCostCol;

    @FXML
    private TableColumn<Part, Integer> allPartsIDCol;

    @FXML
    private TableColumn<Part, Integer> allPartsInventoryCol;

    @FXML
    private TableColumn<Part, String> allPartsNameCol;
    @FXML
    private TableColumn<Part, Double> assocPartsCostCol;

    @FXML
    private TableColumn<Part, Integer> assocPartsIDCol;

    @FXML
    private TableColumn<Part, Integer> assocPartsInventoryCol;

    @FXML
    private TableColumn<Part, String> assocPartsNameCol;

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

    private static ObservableList<Part> newProductParts = null;
    private static int nextProdID = 100;
    Product newProduct = new Product(0,"no name",0.00,0,0,0);


    @FXML
    void onActionSearchPart(ActionEvent event){
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
        addProductTopTableView.setItems(parts);
    }
    @FXML
    void onActionAddPartAssoc(ActionEvent event) {
        Part partToAdd = addProductTopTableView.getSelectionModel().getSelectedItem();
        newProduct.addAssociatedPart(partToAdd);
        addProdPartAssocTableView.setItems(newProduct.getAllAssociatedParts());

    }
    @FXML
    void onActionRemovePartAssoc(ActionEvent event) {
        Part partToDelete = addProductTopTableView.getSelectionModel().getSelectedItem();
        newProduct.deleteAssociatedPart(partToDelete);
    }
    @FXML
    void onActionCancelProductAdd(ActionEvent event) throws IOException {
        InventoryController.confirmCancelAlert();
        Optional<ButtonType> result = InventoryController.confirmCancel.showAndWait();

        if (result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    @FXML
    void onActionSaveProductAdd(ActionEvent event) throws IOException {

        int prodId = nextProdID;
        String prodName = productNameTxt.getText();
        double prodPrice;
        int prodStock;
        int prodStockMin;
        int prodStockMax;
        int i = 0;

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

            while(Inventory.lookupProduct(prodId) != null){
                AddProductController.nextProdID++;
                prodId = nextProdID;
            }
            if (prodName.isBlank()) {
                InventoryController.error = "Product Name";
                InventoryController.invalidNameAlert();
                return;
            }
            if(prodStock < prodStockMin || prodStock > prodStockMax){
                InventoryController.invalidStockAlert();
                return;
            }
            if(prodPrice < 0.00){
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
            Product saveNewProduct = new Product(prodId, prodName, prodPrice, prodStock, prodStockMin, prodStockMax);
            while (i < addProdPartAssocTableView.getItems().size()){
                Part partToAdd = addProdPartAssocTableView.getItems().get(i);
                saveNewProduct.addAssociatedPart(partToAdd);
                i++;
            }
            Inventory.addProduct(saveNewProduct);
            AddProductController.nextProdID++;

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.setTitle("Inventory Management System");
            stage.show();
        }
    }


    /**
     * Method that initializes the AddPartController Class.
     * Sets the top table view and populates with all Parts.
     * Sets the bottom table view for the associated parts.
     */
    @Override
    public void initialize(URL rl, ResourceBundle rb){
        addProductTopTableView.setItems(Inventory.getAllParts());
        allPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        assocPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
