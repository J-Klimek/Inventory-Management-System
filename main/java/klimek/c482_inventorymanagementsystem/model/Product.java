package klimek.c482_inventorymanagementsystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class for creating Product Objects.
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructor for Product objects with parameters id, name, price, stock, min and max.
     * @param id Unique Product ID
     * @param name Product name
     * @param price Product price
     * @param stock Product inventory
     * @param min Product inventory minimum
     * @param max Product inventory maximum
     */
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Gets the Product ID.
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the Product ID.
     * @param id Product ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the Product Name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Product Name.
     * @param name Product Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the Product Price.
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the Product Price.
     * @param price Product Price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the Product Stock also referred to as Inventory.
     * @return stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the Product Stock also referred to as Inventory.
     * @param stock Product Stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Gets the Products minimum Inventory allowed.
     * @return min
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the Products minimum inventory.
     * @param min Product inventory minimum
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Gets the Products maximum inventory.
     * @return max
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the Products maximum inventory.
     * @param max Product inventory maximum
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Method to add associated Part objects to the Product's associatedParts ObservableList.
     * @param part Part object to be added.
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Method to delete associated Part objects from the Product's associatedParts ObservableList.
     * @param selectedAssociatedPart Selected Part to be removed.
     */
    public void deleteAssociatedPart(Part selectedAssociatedPart){
        associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Gets all associated Parts.
     * @return associatedParts
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
