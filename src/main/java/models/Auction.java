package models;

public class Auction {
    private int id,price,Userid;
    float bitcoin;

    private String time,date,Auctionname;

    

    public Auction(int id, int price, int userid, float bitcoin, String time, String date, String Auctionname) {
        this.id = id;
        Userid = userid;
        this.bitcoin = bitcoin;
        this.price = price;
        this.time = time;
        this.date = date;
        this.Auctionname = Auctionname;

    }

    public Auction() {
    }

    public Auction(int price, int userid, float bitcoin, String time, String date, String Auctionname) {
        Userid = userid;
        this.bitcoin = bitcoin;
        this.price = price;
        this.time = time;
        this.date = date;
        this.Auctionname = Auctionname;
    }

    public Auction(int id, int newPrice, String newName, int userid) {
        Userid = userid;
    }

    public Auction(int priceInt, float bitcoinFloat, String auctionTime, String string, String auctionName) {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getBitcoin() {
        return bitcoin;
    }

    public void setBitcoin(float bitcoin) {
        this.bitcoin = bitcoin;
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
    public int getUserid() {
        return Userid;
    }

    public void setUserid(int userid) {
        Userid = userid;
    }

    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ",Userid"+Userid+
                ", time=" + time +
                ", price=" + price +
                ", bitcoin=" + bitcoin +
                ", date=" + date +
                ", Auctionname='" + Auctionname + '\'' +
                '}';
    }
}
