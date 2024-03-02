package Models;

public class Basket {
    private int idB;
    private int quantity;
    private float totalPrice;

    public int getUserid() {
        return Userid;
    }

    public void setUserid(int userid) {
        Userid = userid;
    }

    private int Userid;

    public Basket(int idB, int quantity, float totalPrice) {
        this.idB = idB;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Default constructor
    public Basket() {
        // No need to initialize anything here unless necessary
    }

    // Getters and setters
    public int getIdB() {
        return idB;
    }

    public void setIdB(int idB) {
        this.idB = idB;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Basket{" +
                "idB=" + idB +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
