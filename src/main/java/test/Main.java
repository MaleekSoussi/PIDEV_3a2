package test;

import services.EventService;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        EventService cs = new EventService();
        try {
            System.out.println(cs.readE());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
