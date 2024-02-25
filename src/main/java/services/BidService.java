package services;

import models.Bid;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BidService implements IService<Bid> {
    private Connection connection;

    // Auction ID to be used in methods
    private int auctionId;

    public BidService(int auctionId) {
        connection = MyDatabase.getInstance().getConnection();
        this.auctionId = auctionId;
    }

    public int getHighestBidForAuction(int auctionId) throws SQLException {
        String sql = "SELECT MAX(bidamount) FROM bid WHERE idAuction = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, auctionId);
        ResultSet resultSet = statement.executeQuery();

        int highestBid = 0; // Default value if no bids exist yet

        if (resultSet.next()) {
            highestBid = resultSet.getInt(1);
        }

        resultSet.close();
        statement.close();

        return highestBid;
    }
    @Override
    public void create(Bid bid) throws SQLException {
        int highestBid = getHighestBidForAuction(auctionId);
        System.out.println("Highest Bid: " + highestBid);
        if (bid.getBidAmount() <= highestBid ) { // Increase minimum increment by 1 if needed
            throw new IllegalArgumentException("Bid amount must be at least 1 unit higher than the current highest bid.");
        }

        String sql = "INSERT INTO bid (bidamount, idAuction, userid) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, bid.getBidAmount());
        statement.setInt(2, auctionId );
        statement.setInt(3, 1);

        statement.executeUpdate();
        statement.close();
    }

    @Override
    public void update(Bid bid) throws SQLException {
        String sql = "UPDATE bid SET bidamount = ?, userid = ? WHERE idbid = ? AND idAuction = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, bid.getBidAmount());
        statement.setInt(2, bid.getUserid());
        statement.setInt(3, bid.getIdbid());
        statement.setInt(4, auctionId);

        int rowsAffected = statement.executeUpdate();
        statement.close();

        if (rowsAffected > 0) {
            System.out.println("Bid updated successfully");
        } else {
            System.err.println("Bid update failed");
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM bid WHERE idbid = ? AND idAuction = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.setInt(2, auctionId);

        int rowsAffected = statement.executeUpdate();
        statement.close();

        if (rowsAffected > 0) {
            System.out.println("Bid deleted successfully (ID: " + id + ")");
        } else {
            System.err.println("Bid deletion failed for ID: " + id);
        }
    }

    @Override
    public List<Bid> read() throws SQLException {
        String sql = "SELECT * FROM bid WHERE idAuction = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, auctionId);
        ResultSet rs = statement.executeQuery();
        List<Bid> bids = new ArrayList<>();

        while (rs.next()) {
            Bid bid = new Bid(
                    rs.getInt("idbid"),
                    rs.getInt("idAuction"),
                    rs.getInt("userid"),
                    rs.getInt("bidamount")
            );
            bids.add(bid);
        }

        rs.close();
        statement.close();
        return bids;
    }


}
