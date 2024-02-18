package Test;


import Models.Users;
import Services.UserService;
import Utils.MyDatabase;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        MyDatabase db= new MyDatabase();
        try {
            // Create an instance of UserService
            UserService userService = new UserService();
            userService.create(new Users(0, "hamza", "chaieb", "securepassword123", "hamza.chaieb@example.com", "User", "Active", null, null));
            System.out.println("New user added successfully!");
           // userService.update(new User(1, "hamza", "chaieb", "newpassword123", "hamza.chaieb@example.com", "Admin", "Active", null, null));

            // Read and print out all users
           // System.out.println(userService.read());

//userService.delete(1);

        } catch (SQLException e) {
            System.out.println("Failed: " + e.getMessage());
        }

    }
}
