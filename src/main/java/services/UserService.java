package services;

import models.Users;
import services.IService;
import utils.MyDatabase;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<Users> {

    private Connection connection;
    public static Users currentlyLoggedInUser = null;


    public UserService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(Users user) throws SQLException {
        String sql = "insert into users (firstname, lastname, password, email_address, role, account_status)" +
                " values (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getEmailAddress());
        ps.setString(5, user.getRole());
        ps.setString(6, user.getAccountStatus());
        ps.executeUpdate();
    }

    @Override
    public void update(Users user) throws SQLException {
        String sql = "update users set firstname = ?, lastname = ?, password = ?, email_address = ?, role = ?, account_status = ?, last_login = ? where user_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getEmailAddress());
        ps.setString(5, user.getRole());
        ps.setString(6, user.getAccountStatus());
        ps.setObject(7, user.getLastLogin()); // Using setObject for LocalDateTime
        ps.setInt(8, user.getUserID());
        ps.executeUpdate();
    }

    @Override
    public void delete(int userId) throws SQLException {
        String sql = "delete from users where user_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.executeUpdate();
    }

    @Override
    public List<Users> read() throws SQLException {
        String sql = "SELECT user_id, firstname, lastname, email_address, role, account_status, date_created, last_login FROM users";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Users> users = new ArrayList<>();
        while (rs.next()) {
            Users user = new Users(
                    rs.getInt("user_id"), // Fetch and set the userID
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email_address"),
                    rs.getString("role"),
                    rs.getString("account_status"),
                    rs.getTimestamp("date_created") != null ? rs.getTimestamp("date_created").toLocalDateTime() : null,
                    rs.getTimestamp("last_login") != null ? rs.getTimestamp("last_login").toLocalDateTime() : null
            );
            users.add(user);
        }
        return users;
    }

    public Users authenticate(String emailAddress, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email_address = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, emailAddress);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Users(
                            resultSet.getInt("user_id"),
                            resultSet.getString("firstname"),
                            resultSet.getString("lastname"),
                            resultSet.getString("password"),
                            resultSet.getString("email_address"),
                            resultSet.getString("role"),
                            resultSet.getString("account_status"),
                            resultSet.getObject("date_created", LocalDateTime.class),
                            resultSet.getObject("last_login", LocalDateTime.class)
                    );
                } else {
                    return null;
                }
            }
        }
    }


    public void updateLastLoginTimestamp(String email) throws SQLException {
        String sqlUpdate = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE email_address = ?";
        try (PreparedStatement p = connection.prepareStatement(sqlUpdate)) {
            p.setString(1, email);
            p.executeUpdate();
        }
    }

    public boolean login(String email, String password) {
        try {
            currentlyLoggedInUser = authenticate(email, password);
            return currentlyLoggedInUser != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Users readUser(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Users(
                            rs.getInt("user_id"),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("password"),
                            rs.getString("email_address"),
                            rs.getString("role"),
                            rs.getString("account_status"),
                            rs.getTimestamp("date_created").toLocalDateTime(),
                            rs.getTimestamp("last_login") != null ? rs.getTimestamp("last_login").toLocalDateTime() : null
                    );
                }
            }
        }
        return null;
    }


    public boolean isEmailUnique(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email_address = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // If count is 0, email is unique
                    return rs.getInt(1) == 0;
                }
                return false;
            }
        }
    }



    public void switchView(Stage stage, String fxmlPath) {
        try {
            // Load the view from the given FXML path
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            // Set the new scene to the stage with the loaded root
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading view: " + e.getMessage());
        }
    }


    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: to remove the header text
        alert.setContentText(message);
        alert.showAndWait();
    }

}



