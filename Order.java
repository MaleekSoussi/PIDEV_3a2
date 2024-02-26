package entities;

public class Order {

    private int idO;
    private float totalP;
    private String dateC;
    private String status;
    private int idB; // Add idB field

    public Order(int idO, float totalP, String dateC, String status, int idB) {
        this.idO = idO;
        this.totalP = totalP;
        this.dateC = dateC;
        this.status = status;
        this.idB = idB;
    }


    public Order(int idB, float totalP, String dateC, String status) {
        this.idB = idB;
        this.totalP = totalP;
        this.dateC = dateC;
        this.status = status;
    }



    // Getters and setters

    public int getIdO() {
        return idO;
    }

    public void setIdO(int idO) {
        this.idO = idO;
    }

    public String getDateC() {
        return dateC;
    }

    public void setDateC(String dateC) {
        this.dateC = dateC;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public float getTotalP() {
        return totalP;
    }
    public void setTotalP(float totalP) {
        this.totalP = totalP;
    }
    public int getIdB() {
        return idB;
    }

    public void setIdB(int idB) {
        this.idB = idB;
    }


    @Override
    public String toString() {
        return "Order{" +
                "idO=" + idO +
                ", dateC='" + dateC + '\'' +
                ", totalP=" + totalP +
                ", status='" + status + '\'' +
                '}';
    }}
