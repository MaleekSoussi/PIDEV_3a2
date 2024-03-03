package controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import models.Event;
import models.Ticket;
import services.EventService;
import services.TicketService;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class TicketItemController {
    TicketService ts = new TicketService();
    public static int f;
    Ticket ticket;
    public static Ticket t;

    @FXML
    private AnchorPane apid;

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private Button deleteTicketButtonF;

    private Image qrImage;

    private Event currentEvent;


    public Ticket getSelectedTicket() {
        return ticket;
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void generateAndDisplayQRCode() {
        if (currentEvent != null) {
            String qrCodeData = generateQRCodeData(currentEvent); // Generate data for QR code
            try {
                Image qrImage = generateQRCodeImage(qrCodeData, 200, 200);
                qrCodeImageView.setImage(qrImage); // Set the generated image to ImageView
            } catch (WriterException e) {
                showAlert("Error", "Could not generate QR code: " + e.getMessage());
            }
        }
    }

    // Helper method to generate QR code data string
    private String generateQRCodeData(Event event) {
        // Concatenate event details into a string
        return "Event ID: " + event.getIdE() +
                "\nName: " + event.getNameE() +
                "\nDate: " + event.getDateE() +
                "\nDuration: " + event.getDurationE() +
                "\nType: " + event.getTypeE() +
                "\nEntry Fee: " + event.getEntryFeeE() +
                "\nCapacity: " + event.getCapacityE();
    }

    private Image generateQRCodeImage(String data, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
        generateAndDisplayQRCode();
    }

    @FXML
    void onParticipateButtonClicked(ActionEvent event) {
        if (currentEvent != null) {
            try {
                Ticket newTicket = new Ticket();
                // Set ticket details based on the currentEvent and other necessary information
                // ...

                ts.createT(newTicket); // Save the new ticket to the database
                showAlert("Success", "Your ticket has been generated and added to the database.");
            } catch (SQLException e) {
                showAlert("Database Error", "Failed to create ticket: " + e.getMessage());
            }
        } else {
            showAlert("Error", "No event selected for this ticket.");
        }
    }
}






