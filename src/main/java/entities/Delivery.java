package entities;


public class Delivery {

    private int idD;
    private int  phone_num;
    private String location, status,date, delivery_mode;


    public Delivery(int idD, String location, String date, int phone_num, String delivery_mode) {
        this.idD = idD;
        this.location = location;

        this.date = date;
        this.phone_num = phone_num;
        this.delivery_mode = delivery_mode;
    }

    public Delivery(String location,String date, int phone_num,String delivery_mode ) {

        this.location = location;

        this.date = date;
        this.phone_num = phone_num;
        this.delivery_mode = delivery_mode;

    }

    public int getIdD() {
        return idD;
    }

    public void setIdD(int idD) {
        this.idD = idD;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

   // public int getIdA() {
       // return idA;
 //   }

   // public void setIdA(int idA) {
     //   this.idA = idA;
   // }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   // public int getIdO() {
      //  return idO;
   // }

   // public void setIdO(int idO) {
        //this.idO = idO;
   // }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(int phone_num) {
        this.phone_num = phone_num;
    }

    public String getDelivery_mode() {
        return delivery_mode;
    }

    public void setDelivery_mode(String delivery_mode) {
        this.delivery_mode = delivery_mode;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "idD=" + idD +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", phone_num=" + phone_num +
                ", delivery_mode='" + delivery_mode + '\'' +
                '}';
    }
}
