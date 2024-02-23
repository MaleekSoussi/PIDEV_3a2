package models;

public class AuctionItem {
    private String name;
    private double price;
    private double bitcoinPrice;

    public AuctionItem(String name, double price, double bitcoinPrice) {
        this.name = name;
        this.price = price;
        this.bitcoinPrice = bitcoinPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getBitcoinPrice() {
        return bitcoinPrice;
    }

    public void setBitcoinPrice(double bitcoinPrice) {
        this.bitcoinPrice = bitcoinPrice;
    }

    // Constructor, getters, and setters
}