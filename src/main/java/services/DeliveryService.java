package services;

import entities.Delivery;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryService implements IService<Delivery> {
    private final Connection connection;

    public DeliveryService() {
        connection = MyDB.getInstance().getConnection();
    }

    @Override
    public void create(Delivery delivery) throws SQLException {
        String req = "INSERT INTO delivery (location,status, date, phone_num, delivery_mode ) VALUES (?, 'Pending',?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(req)) {
            pstmt.setString(1, delivery.getLocation());
            pstmt.setString(2, delivery.getDate());
            pstmt.setInt(3, delivery.getPhone_num());
            pstmt.setString(4, delivery.getDelivery_mode());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void update(Delivery delivery, int idD) throws SQLException {
        String sql = "UPDATE delivery SET location=?, status='Pending',date=?, phone_num=?, delivery_mode=? WHERE idD=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, delivery.getLocation());
            ps.setString(2, delivery.getDate());
            ps.setInt(3, delivery.getPhone_num());
            ps.setString(4, delivery.getDelivery_mode());
            ps.setInt(5, idD);

            ps.executeUpdate();
        }
    }


    @Override
    public void delete(int idD) throws SQLException {
        String sql = "DELETE FROM delivery WHERE idD=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idD);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Delivery> read() throws SQLException {
        String sql = "SELECT * FROM delivery";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            List<Delivery> deliveries = new ArrayList<>();
            while (rs.next()) {
                int idD = rs.getInt("idD");
                String location = rs.getString("location");
                String date = rs.getString("date");
                int phone_num = rs.getInt("phone_num");
                String delivery_mode = rs.getString("delivery_mode");

                Delivery delivery = new Delivery(idD, location, date, phone_num, delivery_mode);
                deliveries.add(delivery);
            }
            return deliveries;
        }
    }
}
