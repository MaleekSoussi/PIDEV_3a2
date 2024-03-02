package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import Models.Workshop;
import Services.WorkshopService;

import java.io.IOException;
import java.sql.SQLException;

public class AddWorkshopController {
    private final WorkshopService ws = new WorkshopService();


    @FXML
    private TextField namewf;

    @FXML
    private TextField ressourcesf;

    @FXML
    private TextField descaf;

    @FXML
    private TextField durationf;





    @FXML
    void addworkshop(ActionEvent event)
    {
        try
        {
            ws.createw(new Workshop(namewf.getText(), ressourcesf.getText(), descaf.getText(), Float.parseFloat(durationf.getText()),ShowCoursesController.idc));
            ShowCoursesController.nbwc++;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("success");
            alert.setContentText("Workshop added successfully ");
            alert.showAndWait();
            namewf.setText("");
            ressourcesf.setText("");
            descaf.setText("");
            durationf.setText("");
        }
        catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/showWorkshops.fxml"));
            durationf.getScene().setRoot(root);
        } catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }

    @FXML
    void returna(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/showWorkshops.fxml"));
            durationf.getScene().setRoot(root);
        } catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }
}
