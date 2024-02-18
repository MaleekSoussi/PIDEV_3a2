package test;

import entities.art;
import services.ArtServices;
import utils.MyDB;


import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MyDB conn1 = MyDB.getInstance();
        ArtServices s = new ArtServices();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Add ART");
            System.out.println("2. Display ARTS");
            System.out.println("3. Update ART");
            System.out.println("4. Delete ART");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Add ART
                    art newArt = getARTInput();
                    try {
                        s.add(newArt);
                        System.out.println("art added successfully!");
                    } catch (SQLException e) {
                        System.out.println("Error adding ART: " + e.getMessage());
                    }
                   break;
                case 2:
                    // Display ART
                    try {
                        System.out.println(s.display());
                    } catch (SQLException e) {
                        //e.printStackTrace();
                        System.out.println("Error displaying users: " + e.getMessage());
                    }
                    break;
                case 3:
                    // Update ART
                    System.out.print("Enter art1 ID to update: ");
                    int artIdToUpdate = scanner.nextInt();
                    art updatedArt = getARTInput();
                    try {
                        s.modify(updatedArt, artIdToUpdate);
                        System.out.println("ART updated successfully!");
                    } catch (SQLException e) {
                        System.out.println("Error updating ART: " + e.getMessage());
                    }

                    break;
                case 4:
                    // Delete ART
                    System.out.print("Enter art ID to delete: ");
                    int artIdToDelete = scanner.nextInt();
                    try {
                        s.delete(artIdToDelete);
                        System.out.println("ART deleted successfully!");
                    } catch (SQLException e) {
                        System.out.println("Error deleting ART: " + e.getMessage());
                    }
                    break;
                case 5:
                    // Exit
                    System.out.println("Exiting program.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }


    private static art getARTInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter art title: ");
        String title = scanner.next();
        System.out.print("Enter art materials: ");
        String materials = scanner.next();
        System.out.print("Enter art height: ");
        double height = scanner.nextDouble();
        System.out.print("Enter art width: ");
        double width = scanner.nextDouble();
        System.out.print("Enter art type: ");
        String type = scanner.next();
        System.out.print("Enter art city: ");
        String city = scanner.next();
        System.out.print("Enter art description: ");
        String description = scanner.next();
        return new art( title, materials, height, width, type,city,description);
    }

}
