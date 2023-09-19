package klimek.c482_inventorymanagementsystem.model;

/**
 * Class for creating Outsourced Part Objects.
 */
public class OutSourced extends Part{
    private String companyName;

    /**
     * Constructor for creating Outsourced Part Objects.
     * @param id Unique Part ID
     * @param name Part name
     * @param price Part price
     * @param stock Part inventory
     * @param min Part inventory minimum
     * @param max Part inventory maximum
     * @param companyName Outsourced company name
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Gets the outsourced company name.
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the outsourced company name.
     * @param companyName Outsourced companyName attribute
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
