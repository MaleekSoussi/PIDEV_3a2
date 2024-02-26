package services;

import models.Event;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService implements IService<Event> {

    private Connection connection;

    public EventService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public Event create(Event event) throws SQLException {
        if (event == null) {
            event = new Event(); // Instantiate a new Event if it's null
        }

        String sql = "INSERT INTO event (nameE, dateE, durationE, typeE, entryFeeE, capacityE) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, event.getNameE());
            ps.setDate(2, Date.valueOf(event.getDateE()));
            ps.setInt(3, event.getDurationE());
            ps.setString(4, event.getTypeE());
            ps.setDouble(5, event.getEntryFeeE());
            ps.setInt(6, event.getCapacityE());
            ps.executeUpdate();

            // Retrieve the generated keys (including the ID)
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Update the ID of the event
                    event.setIdE(generatedKeys.getInt(1));
                }
            }

            // Return the created or modified event
            return event;
        }
    }


    @Override
    public void update(Event event) throws SQLException {
        String sql = "UPDATE event SET nameE = ?, dateE = ?, durationE = ?, typeE = ?, entryFeeE = ?, capacityE = ? WHERE idE = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, event.getNameE());
            ps.setDate(2, Date.valueOf(event.getDateE()));
            ps.setInt(3, event.getDurationE());
            ps.setString(4, event.getTypeE());
            ps.setDouble(5, event.getEntryFeeE());
            ps.setInt(6, event.getCapacityE());
            ps.setInt(7, event.getIdE());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int idE) throws SQLException {
        String sql = "DELETE FROM event WHERE idE = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idE);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Event> read(String eventName) throws SQLException {
        String sql = "SELECT * FROM event WHERE nameE = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, eventName);

            try (ResultSet rs = statement.executeQuery()) {
                List<Event> events = new ArrayList<>();

                while (rs.next()) {
                    Event event = new Event(
                            rs.getInt("idE"),
                            rs.getString("nameE"),
                            rs.getDate("dateE").toLocalDate(),
                            rs.getInt("durationE"),
                            rs.getString("typeE"),
                            rs.getDouble("entryFeeE"),
                            rs.getInt("capacityE"),
                            rs.getString("imageE")
                    );
                    events.add(event);
                }

                return events;
            }
        }
    }

    public int getEventIdByName(String eventName) throws SQLException {
        // Assuming you have a database connection established as 'connection'
        final String query = "SELECT id FROM event WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, eventName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    throw new SQLException("Event not found with name: " + eventName);
                }
            }
        }
    }

    public String getDetails(String eventName) throws SQLException {
        // Assume you have a method to get a database connection
        String details = "";

        // SQL query to retrieve event details
        String sql = "SELECT details FROM event WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, eventName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    details = rs.getString("details");
                } else {
                    // Handle the case where no event with the given name is found
                    // For example, throw an exception or return a default message
                    throw new SQLException("No event found with the name: " + eventName);
                }
            }
        } catch (SQLException e) {
            // Here you would handle any SQL exceptions that occur during the query
            throw e;
        } finally {
            // Make sure to close the connection
            if (connection != null) {
                connection.close();
            }
        }

        return details;
    }

    // EventService class
    @Override
    public Event readById(int idE) throws SQLException {
        String sql = "SELECT * FROM event WHERE idE = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idE);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Event(
                            rs.getInt("idE"),
                            rs.getString("nameE"),
                            rs.getDate("dateE").toLocalDate(),
                            rs.getInt("durationE"),
                            rs.getString("typeE"),
                            rs.getDouble("entryFeeE"),
                            rs.getInt("capacityE"),
                            rs.getString("imageE")
                    );
                }
            }
        }
        return null;
    }

    public List<Event> getAllEvents() throws SQLException {
        String sql = "SELECT * FROM event";
        List<Event> events = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Event event = new Event(
                            rs.getInt("idE"),
                            rs.getString("nameE"),
                            rs.getDate("dateE").toLocalDate(),
                            rs.getInt("durationE"),
                            rs.getString("typeE"),
                            rs.getDouble("entryFeeE"),
                            rs.getInt("capacityE"),
                            rs.getString("imageE")
                    );
                    events.add(event);
                }
            }
        }
        return events;
    }

}
