package klimek.c482_inventorymanagementsystem.model;

/**
 * Class for creating InHouse Part objects
 *
 * @author James Klimek
 */
public class InHouse extends Part{

    private int machineID;

    /**
     * Constructor for InHouse Part objects passing in the id, name, price, stock, min, max, and productID as parameters
     * @param id Unique Part ID
     * @param name Part name string
     * @param price Part price double
     * @param stock Part inventory amount
     * @param min Part minimum allowed inventory
     * @param max Part maximum allowed inventory
     * @param machineID Part In House machineID
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * Gets the Machine ID.
     * @return machineID
     */
    public int getMachineID() {
        return machineID;
    }

    /**
     * Sets the Product ID.
     * @param machineID In-house Machine ID
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
