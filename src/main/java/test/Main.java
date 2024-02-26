package test;

import models.Event;
import services.EventService;
import services.TicketService;
import utils.MyDatabase;
import models.Ticket;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        MyDatabase db = new MyDatabase();

        try {
            EventService eventService = new EventService();
            TicketService ticketService = new TicketService();

            // Parse the date string to create a LocalDate object
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate eventDate = LocalDate.parse("02-01-2024", formatter);

            // Create and add an Event
            Event event = new Event(1, "Sample Event", eventDate, 2, "Individual", 80, 50, "imagePath");
            eventService.create(event);


            System.out.println("New Event added successfully! Event ID: " + event.getIdE());

            // Create and add a Ticket with reference to the Event
            Ticket ticket = new Ticket(1, event, "QRCode123");
            System.out.println("Ticket created with Event ID: " + ticket.getEvent().getIdE());
            ticketService.create(ticket);

            System.out.println("New Ticket added successfully!");

            // Read and print out all events and tickets
            System.out.println("Events:");
            System.out.println(eventService.read("Test Event"));

            System.out.println("Tickets:");
            System.out.println(ticketService.read("90210"));

        } catch (SQLException e) {
            System.out.println("Failed: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for detailed error information
        }
    }
}
