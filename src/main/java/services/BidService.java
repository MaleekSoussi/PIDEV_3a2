package services;

import models.Bid;
import utils.MyDatabase;
import models.Auction;
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

    public Auction getAuctionById(int auctionId) throws SQLException {
        Auction auction = null;
        String sql = "SELECT * FROM auction WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, auctionId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            auction = new Auction(
                    resultSet.getInt("id"),
                    resultSet.getInt("price"),
                    resultSet.getInt("userid"), // Assuming there's a userid column in the auction table
                    resultSet.getFloat("bitcoin"),
                    resultSet.getString("time"),
                    resultSet.getString("date"),
                    resultSet.getString("Auctionname")
            );
        }

        resultSet.close();
        statement.close();

        return auction;
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
        // Fetch the price of the auction
        AuctionService auctionService = new AuctionService();
        Auction auction = auctionService.getAuctionById(auctionId); // Assuming you have a method to get auction by ID

        // Set the initial highest bid to be the price of the auction
        int highestBid = auction.getPrice();

        // Check if there are existing bids for the auction and update the highest bid if necessary
        int currentHighestBid = getHighestBidForAuction(auctionId);
        if (currentHighestBid > highestBid) {
            highestBid = currentHighestBid;
        }

        // Validate the bid amount against the highest bid
        if (bid.getBidAmount() <= highestBid) {
            throw new IllegalArgumentException("Bid amount must be higher than the current highest bid.");
        }

        // Proceed with creating the bid
        String sql = "INSERT INTO bid (bidamount, idAuction, Userid) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, bid.getBidAmount());
        statement.setInt(2, auctionId);
        statement.setInt(3, 1); // Assuming you want to use the userid from the Bid object

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
