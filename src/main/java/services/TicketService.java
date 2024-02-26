package services;

import models.Event;
import models.Ticket;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketService implements IService<Ticket> {

    private Connection connection;
    private EventService eventService; // To fetch associated events

    public TicketService() {
        connection = MyDatabase.getInstance().getConnection();
        eventService = new EventService();
    }

    public Ticket create(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO ticket (idE, qrCodeT) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ticket.getEvent().getIdE());
            ps.setString(2, ticket.getQrCodeT());
            ps.executeUpdate();

            // Retrieve the auto-generated key (idT)
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ticket.setIdT(generatedKeys.getInt(1));
                }
            }

            // Add a return statement here
            return ticket;
        }
    }


    @Override
    public void update(Ticket ticket) throws SQLException {
        String sql = "UPDATE ticket SET idE = ?, qrCodeT = ? WHERE idT = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ticket.getEvent().getIdE());
            ps.setString(2, ticket.getQrCodeT());
            ps.setInt(3, ticket.getIdT());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int idT) throws SQLException {
        String sql = "DELETE FROM ticket WHERE idT = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idT);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Ticket> read(String ticketCode) throws SQLException {
        String sql = "SELECT * FROM ticket";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            List<Ticket> tickets = new ArrayList<>();

            while (rs.next()) {
                int ticketId = rs.getInt("idT");
                int eventId = rs.getInt("idE");
                String qrCode = rs.getString("qrCodeT");

                // Fetch the associated event from EventService
                Event event = eventService.readById(eventId);

                Ticket ticket = new Ticket(ticketId, event, qrCode);
                tickets.add(ticket);
            }
            return tickets;
        }
    }


    @Override
    public Ticket readById(int idE) throws SQLException {
        return null;
    }

}
