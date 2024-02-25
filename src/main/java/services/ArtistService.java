package services;

import models.Auction;
import models.Users;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArtistService implements IService<Auction> {
    private Connection connection;

    public ArtistService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(Auction auction) throws SQLException {
        String sql = "INSERT INTO auction (Auctionname, price, bitcoin, time, date) VALUES (?, ?, ?, ?, ?) " ;
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, auction.getAuctionname());
        statement.setInt(2, auction.getPrice());
        statement.setFloat(3, auction.getBitcoin());
        statement.setString(4, auction.getTime());
        statement.setString(5, auction.getDate());

        statement.executeUpdate();
        statement.close();
    }

    @Override
    public void update(Auction auction) throws SQLException {
        String sql = "UPDATE auction SET Auctionname = ?, price = ?, bitcoin = ?, time = ?, date = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, auction.getAuctionname());
        statement.setInt(2, auction.getPrice());
        statement.setFloat(3, auction.getBitcoin());
        statement.setString(4, auction.getTime());
        statement.setString(5, auction.getDate());
        statement.setInt(6, auction.getId());

        int rowsAffected = statement.executeUpdate();
        statement.close();

        if (rowsAffected > 0) {
            System.out.println("Auction updated successfully");
        } else {
            System.err.println("Auction update failed");
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM auction WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);

        int rowsAffected = statement.executeUpdate();
        statement.close();

        if (rowsAffected > 0) {
            System.out.println("Auction deleted successfully (ID: " + id + ")");
        } else {
            System.err.println("Auction deletion failed for ID: " + id);
        }
    }

    @Override
    public List<Auction> read() throws SQLException {
        String sql = "SELECT * FROM auction WHERE Userid=2";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Auction> auctions = new ArrayList<>();

        while (rs.next()) {
            Auction auction = new Auction();
            auction.setId(rs.getInt("id"));
            auction.setPrice(rs.getInt("price"));
            auction.setBitcoin(rs.getFloat("bitcoin"));
            auction.setTime(rs.getString("time"));
            auction.setDate(rs.getString("date"));
            auction.setAuctionname(rs.getString("Auctionname"));
            auctions.add(auction);
        }

        rs.close();
        statement.close();
        return auctions;
    }



}

