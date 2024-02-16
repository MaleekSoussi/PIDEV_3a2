package models;

public class Auction {
    private int id,price;
    private String Auctionname;

    public Auction() {
    }

    public Auction(int id, int price, String Auctionname) {
        this.id = id;
        this.price = price;
        this.Auctionname = Auctionname;
    }


    public Auction(int price, String Auctionname) {
        this.price = price;
        this.Auctionname = Auctionname;
    }

    public Auction(int id, String newName, int newPrice) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public  String getAuctionname() {
        return Auctionname;
    }

    public void setAuctionname(String Auctionname) {this.Auctionname=Auctionname;
    }

    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", price=" + price +
                ", Auctionname='" + Auctionname + '\'' +
                '}';
    }
}
