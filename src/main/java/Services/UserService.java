package Services;

import Models.Users;
import Utils.MyDatabase;

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


    @Override
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

    public void updatesettings(Users user) throws SQLException {
        String sql = "UPDATE users SET firstname = ?, lastname = ?, email_address = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmailAddress());
            ps.setInt(4, user.getUserID());
            ps.executeUpdate();
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



}




