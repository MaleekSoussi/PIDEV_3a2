package test;

import entities.Delivery;
import services.DeliveryService;
import utils.MyDB;

import java.sql.SQLException;

public class main {

    public static void main(String[] args) {
        DeliveryService ps = new DeliveryService();
        try {
//            ps.create(new Person(23,"insert into ","Ben Foulen"));
//            ps.update(new Person(1,25, "Skander","Ben Foulen"));
            System.out.println(ps.read());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
