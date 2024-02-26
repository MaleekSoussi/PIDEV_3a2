package services;

import entities.Basket;
import utils.MyDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BasketService implements IService<Basket> {
    private Connection connection;

    public BasketService() {
        connection = MyDB.getInstance().getConnection();
    }

    public int create(Basket basket) {
        int generatedId = 0;
        String sql = "INSERT INTO basket (quantity, total_price) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, basket.getQuantity());
            pstmt.setFloat(2, basket.getTotalPrice());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                        basket.setIdB(generatedId); // Set the generated ID back to the Basket object
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return generatedId;
    }






    @Override
    public void update(Basket basket, int idB) throws SQLException {
        String sql = "UPDATE basket SET quantity=?, total_price=? WHERE idB=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, basket.getQuantity());
            ps.setFloat(2, basket.getTotalPrice());
            ps.setInt(3, idB);
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int idB) throws SQLException {
        String sql = "DELETE FROM basket WHERE idB=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idB);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Basket> read() throws SQLException {
        List<Basket> baskets = new ArrayList<>();
        String sql = "SELECT * FROM basket";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                int idB = rs.getInt("idB");
                int quantity = rs.getInt("quantity");
                float total_price = rs.getFloat("total_price");
                Basket basket = new Basket(idB, quantity, total_price);
                baskets.add(basket);
            }
        }
        return baskets;
    }

    public Basket getBasketById(int idB) throws SQLException {
        String sql = "SELECT * FROM basket WHERE idB = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idB);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int quantity = rs.getInt("quantity");
                float total_price = rs.getFloat("total_price");
                return new Basket(idB, quantity, total_price); // Adjusted to match the updated Basket constructor.
            }
        }
        return null;
    }
    public int getLatestBasketId() {
        int id = 0;
        String sql = "SELECT idB FROM basket ORDER BY idB DESC LIMIT 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                id = rs.getInt("idB");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


    public boolean exists(int idB) throws SQLException {
        String sql = "SELECT COUNT(*) FROM basket WHERE idB = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idB);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                // If the count is greater than 0, the idB exists
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }





}
