package klimek.c482_inventorymanagementsystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import klimek.c482_inventorymanagementsystem.InventoryController;

/**
 * The Inventory class with methods for looking up, adding/deleting/updating Part and Product Objects and lists.
 */

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Static method that passes in a Part object and adds it to the allParts ObservableList.
     * @param newPart A new Part Object.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    /**
     * Static method that passes in a Product object and adds it to the allProducts ObservableList.
     * @param newProduct A new Product Object.
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }
    /**
     * Static method that passes in a Part object's ID attribute and if found returns the Part Object else returns null.
     * @param partId Part object's ID.
     * @return The Part Object that matches the parameter's ID or null.
     */
    public static Part lookupPart(int partId){
        ObservableList<Part> allParts = Inventory.getAllParts();
        for(Part part : allParts){
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     * Static method that passes in a Product object's ID attribute and if found returns the Product object else returns null.
     * @param productId Product Object's ID.
     * @return The Product object that matches the parameter's ID or null.
     */
    public static Product lookupProduct(int productId){
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        for(Product product:allProducts){
            if(product.getId() == productId){
                return product;
            }
        }
        return null;
    }
    /**
     * Static method that passes in a Part object's name attribute and if found returns an ObservableList of Part objects.
     * @param partName Part object's name.
     * @return ObservableList of Part objects containing the parameter's name.
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> namedParts = FXCollections.observableArrayList();

        ObservableList<Part> allParts = Inventory.getAllParts();
        for(Part part:allParts){
            if(part.getName().contains(partName)){
                namedParts.add(part);
            }
        }

        return namedParts;
    }
    /**
     * Static method that passes in a Product object's name attribute and if found returns an ObservableList of Product objects.
     * @param productName Part object's name.
     * @return ObservableList of Product objects containing the parameter's name.
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> namedProducts = FXCollections.observableArrayList();

        ObservableList<Product> allProducts = Inventory.getAllProducts();
        for(Product product:allProducts){
            if(product.getName().contains(productName)){
                namedProducts.add(product);
            }
        }

        return namedProducts;
    }

    /**
     * Static method that passes in an index and a Part Object and updates the allParts ObservableList.
     * Sets the allParts ObservableList at the position of index with the selectedPart Object.
     * @param index Index location of the selectedPart object.
     * @param selectedPart Part object that will update the list at the index parameter.
     */
    public static void updatePart(int index, Part selectedPart){
        Inventory.getAllParts().set(index, selectedPart);
    }
    /**
     * Static method that passes in an index and a Product Object and updates the allProducts ObservableList.
     * Sets the allProducts ObservableList at the position of index with the selectedProduct Object.
     * @param index Index location of the selectedProduct object.
     * @param newProduct Product object that will update the list at the index parameter.
     */
    public static void updateProduct(int index, Product newProduct){
        Inventory.getAllProducts().set(index,newProduct);
    }

    /**
     * Static method that passes in a Part object to be deleted.
     * Triggers Alert pop-up if selectedPart parameter is null.
     * @param selectedPart Part object to be deleted.
     * @return False if selectedPart is null else True.
     */
    public static boolean deletePart(Part selectedPart){
        if (selectedPart == null) {
            InventoryController.invalidSelectionAlert();
            return false;
        } else {
            return true;
        }
    }
    /**
     * Static method that passes in a Product object to be deleted.
     * Triggers Alert pop-up if selectedProduct parameter is null or has associated Parts.
     * @param selectedProduct Product object to be deleted.
     * @return False if selectedProduct is null or has associated Parts else True.
     */
    public static boolean deleteProduct(Product selectedProduct) {

        if (selectedProduct == null) {
            InventoryController.invalidSelectionAlert();
            return false;
        }
        if (selectedProduct.getAllAssociatedParts().size() > 0) {
            InventoryController.productHasAssocPartsAlert();
            return false;
        }
        else {
            return true;
        }

    }

    /**
     * Static method that returns the allParts ObservableList. 
     * @return allParts ObservableList.
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * Static method that returns the allProducts ObservableList.
     * @return allProducts ObservableList.
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}

