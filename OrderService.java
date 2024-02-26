package services;

import entities.Basket;
import entities.Order;
import utils.MyDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService implements IService<Order> {
    private final Connection connection;

    public OrderService() {
        connection = MyDB.getInstance().getConnection();
    }

    @Override
    public int create(Order order) throws SQLException {

        System.out.println("Debug: idB being inserted = " + order.getIdB());
        if (!isIdBValid(order.getIdB())) {
            throw new SQLException("Foreign key constraint violation: idB " + order.getIdB() + " does not exist in the basket table.");
        }
        String sql = "INSERT INTO `order` (totalP, dateC, status, idB) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setFloat(1, order.getTotalP());
            pstmt.setString(2, order.getDateC());
            pstmt.setString(3, order.getStatus());
            pstmt.setInt(4, order.getIdB());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setIdO(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
        return order.getIdO();
    }

    private boolean isIdBValid(int idB) throws SQLException {
        String sql = "SELECT COUNT(*) FROM basket WHERE idB = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idB);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // If the count is not zero, then the idB exists
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


    @Override
    public void update(Order order, int idO) throws SQLException {
        String sql = "UPDATE `order` SET totalP=?, dateC=?, status=?, idB=? WHERE idO=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setFloat(1, order.getTotalP());
            ps.setString(2, order.getDateC());
            ps.setString(3, order.getStatus());
            ps.setInt(4, order.getIdB());
            ps.setInt(5, idO);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating order failed, no rows affected.");
            }
        }
    }

    @Override
    public void delete(int idO) throws SQLException {
        String sql = "DELETE FROM `order` WHERE idO=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idO);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting order failed, no rows affected.");
            }
        }
    }

    @Override
    public List<Order> read() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.idO, o.totalP, o.dateC, o.status, b.idB, b.quantity, b.total_price " +
                "FROM `order` o " +
                "JOIN basket b ON o.idB = b.idB";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                int idO = rs.getInt("idO");
                float totalP = rs.getFloat("totalP");
                String dateC = rs.getString("dateC");
                String status = rs.getString("status");
                int idB = rs.getInt("idB");
                int quantity = rs.getInt("quantity");
                float total_price = rs.getFloat("total_price");

                Order order = new Order(idO, totalP, dateC, status, idB);
                // Assuming you have a method to set basket details in Order class
                orders.add(order);
            }
        }
        return orders;
    }

    public Order getOrder(int idO) throws SQLException {
        String sql = "SELECT * FROM `order` WHERE idO = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idO); // Set the idO parameter in the query
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Extract the order details from the ResultSet
                    float totalP = rs.getFloat("totalP");
                    String dateC = rs.getString("dateC");
                    String status = rs.getString("status");
                    int idB = rs.getInt("idB");

                    // Create and return a new Order object with the extracted details
                    return new Order(idO, totalP, dateC, status, idB);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to allow the caller to handle it
        }
        return null; // Return null if no order is found with the specified ID
    }


}
