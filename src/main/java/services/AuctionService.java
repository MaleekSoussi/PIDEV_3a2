package services;

import models.Person;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import models.Auction;
import java.sql.*;
import java.util.ArrayList;


public class AuctionService implements IService<Auction>{
    private Connection connection;

    public AuctionService(){
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override

    public void create(Auction Auction) throws SQLException {
        String sql = "insert into auction(Auctionname,price)"+
                "values('"+Auction.getAuctionname()+"','"+Auction.getPrice()+"'" + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);

    }

    @Override
    public void update(Auction auction) throws SQLException {

        String sql = "UPDATE auction SET Auctionname = ?, price = ? WHERE id = ?";

        // Prepare statement and set parameters
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, auction.getAuctionname());
        statement.setInt(2, auction.getPrice());
        statement.setInt(3, auction.getId());

        // Execute update and handle success/failure
        int rowsAffected = statement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Auction updated successfully");
            // Update UI or trigger refresh (if applicable)
        } else {
            System.err.println("Auction update failed");
            // Handle update error appropriately
        }

        // Close connection
        connection.close();
    }

    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM auction WHERE id = ?";

        // Prepare the statement and set the parameter
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);

        // Execute the DELETE query
        int rowsAffected = statement.executeUpdate();

        // Check if delete was successful
        if (rowsAffected > 0) {
            System.out.println("Auction deleted successfully (ID: " + id + ")");
            // Update UI or inform user about success (if applicable)
        } else {
            System.err.println("Auction deletion failed for ID: " + id);
            // Handle deletion failure appropriately (e.g., log error, inform user)
        }

        // Close the connection
        connection.close();
    }


    @Override
    public List<Auction> read() throws SQLException {
        String sql = "select * from auction";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Auction> auction = new ArrayList<>();
        while (rs.next()){
            Auction p = new Auction();
            p.setId(rs.getInt("id"));
            p.setPrice(rs.getInt("price"));
            p.setAuctionname(rs.getString("Auctionname"));


            auction.add(p);
        }
        return auction;
    }

}


