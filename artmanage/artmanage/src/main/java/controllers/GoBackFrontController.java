package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class GoBackFrontController {

    @FXML
    private Button Back;

    @FXML
    private Button Front;

    @FXML
    void GoToFront(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FronClient.fxml"));
            Back.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }



    @FXML
    void goToBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ManageArtist.fxml"));
            Back.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }

}
