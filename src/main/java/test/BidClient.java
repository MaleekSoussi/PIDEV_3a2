package test;

import com.google.gson.Gson;
import controllers.Client.ViewBidUsers;
import javafx.application.Platform;
import models.Bid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BidClient {
    private String host;
    private int port;
    private int auctionId;
    private ViewBidUsers viewBidUsers;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public BidClient(String host, int port, int auctionId, ViewBidUsers viewBidUsers) {
        this.host = host;
        this.port = port;
        this.auctionId = auctionId;
        this.viewBidUsers = viewBidUsers;
    }

    public void connectBidClient(int auctionId) throws IOException {
        this.auctionId = auctionId;
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Send a join message or any initial handshake message if required
        sendJoinMessage();

        // Start a new thread to listen for messages from the server
        new Thread(() -> {
            try {
                String fromServer;
                while ((fromServer = in.readLine()) != null) {
                    // Log bid message received from server
                    System.out.println("Received bid message from server: " + fromServer);
                    // Process the message received from the server
                    Bid bid = convertToBid(fromServer);
                    Platform.runLater(() -> {
                        // Update UI based on the new bid information
                        viewBidUsers.updateTableView(bid);
                        viewBidUsers.refreshHighestBid();
                    });
                }
            } catch (IOException e) {
                // Handle exception
            } finally {
                // Close connections
            }
        }).start();
    }


    public Bid convertToBid(String bidUpdate) {
        Gson gson = new Gson();
        // Assuming the format is "BID {json}", we need to extract the JSON part.
        String jsonPart = bidUpdate.substring(bidUpdate.indexOf("{"));
        return gson.fromJson(jsonPart, Bid.class);
    }


    private void sendJoinMessage() {
        // Example of sending a join message to the server
        out.println("JOIN " + auctionId);
    }

    public void sendBid(String bidMessage) {
        out.println("BID " + bidMessage);
    }

    private void closeConnections() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
