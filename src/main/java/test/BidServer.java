package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class BidServer {
    private static final int PORT = 8001;
    private static final Map<Integer, BidRoom> bidRooms = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Bid Server is running on port " + PORT);
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class BidRoom {
        private final Set<PrintWriter> clients = new CopyOnWriteArraySet<>();

        synchronized void broadcastBid(String bidMessage) {
            for (PrintWriter client : clients) {
                client.println(bidMessage);
            }
        }

        void addClient(PrintWriter client) {
            clients.add(client);
        }

        void removeClient(PrintWriter client) {
            clients.remove(client);
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in; // Add BufferedReader for reading from the client
        private BidRoom currentRoom;
        private int auctionId;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Initialize BufferedReader
                // Read join message from client
                String joinMessage = in.readLine(); // Read join message from client
                handleJoinMessage(joinMessage);

                String fromClient;
                while ((fromClient = in.readLine()) != null) {
                    if (fromClient.startsWith("BID ")) {
                        // Handle bid message from client
                        // Example: BID {"auctionId":123,"bidAmount":50,"userId":456}
                        handleBidMessage(fromClient);
                    }
                }
            } catch (IOException e) {
                // Handle exception
            } finally {
                // Close connections
                if (out != null) {
                    out.close();
                }
                if (in != null) { // Close BufferedReader
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // Remove client from room
                if (currentRoom != null) {
                    currentRoom.removeClient(out);
                }
            }
        }

        private void handleJoinMessage(String joinMessage) {
            // Extract auctionId from join message
            // Example: JOIN 123
            String[] parts = joinMessage.split(" ");
            if (parts.length == 2) {
                try {
                    auctionId = Integer.parseInt(parts[1]);
                    currentRoom = bidRooms.computeIfAbsent(auctionId, k -> new BidRoom());
                    currentRoom.addClient(out);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid auction ID received: " + parts[1]);
                }
            } else {
                System.out.println("Invalid join message: " + joinMessage);
            }
        }

        private void handleBidMessage(String bidMessage) {
            if (currentRoom != null) {
                currentRoom.broadcastBid(bidMessage);
            } else {
                System.out.println("No room found for auction ID: " + auctionId);
            }
        }
    }
}