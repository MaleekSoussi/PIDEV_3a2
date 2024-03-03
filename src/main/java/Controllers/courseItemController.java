package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import Models.Courses;
import Services.CoursesService;

import java.io.IOException;
import java.sql.SQLException;

public class courseItemController
{
    CoursesService cs = new CoursesService();
    private ShowWorkshopsFController showWorkshopsFController;
    public static int idc;
    private Courses course;
    public Courses selectedcourse;
    public static int i;
    private UpdateCourseFController updateCourseFController;
    private String imagePath;
    private ShowCoursesFController showCoursesFController;
    public void updateImage(Image image) {
        imageC.setImage(image);
    }

    public Courses getSelectedCourse()
    {
        return course;
    }

    @FXML
    public static AnchorPane apid;

    @FXML
    private Label descCI;

    @FXML
    private Label nameCI;

    @FXML
    private Label nbwCI;

    @FXML
    private Label priceCI;

    @FXML
    private Label typeCI;

    @FXML
    private Button showlistbutton;

    @FXML
    private Button deletecoursebuttonF;

    @FXML
    private Button editcoursebuttonF;

    @FXML
    private ImageView imageC;

    public void setParent(ShowCoursesFController showCoursesFController)
    {
        this.showCoursesFController=showCoursesFController;
    }

    public void setInstance(ShowCoursesFController showCoursesFController)
    {
        this.showCoursesFController=showCoursesFController;
    }
    public void setUpdateCourseFController(UpdateCourseFController updateCourseFController) {
        this.updateCourseFController = updateCourseFController;
    }

    public void updateImageFromUpdateCourse(Image image) {
        imageC.setImage(image);
    }

    public void get_idc(Courses course)
    {
        idc=course.getId_C();
    }

    public void setData(Courses course)
    {
        nameCI.setText(course.getNameC());
        descCI.setText(course.getDescriptionC());
        priceCI.setText(String.valueOf(course.getPriceC()));
        typeCI.setText(course.getType());
        nbwCI.setText(String.valueOf(course.getNumberW()));
        Image image = new Image(course.getImage_path());
        imageC.setImage(image);
        double size = 330;
        double cornerRadius = 30;

        Rectangle clip = new Rectangle(size, size);
        clip.setArcWidth(cornerRadius * 2);
        clip.setArcHeight(cornerRadius * 2);
        imageC.setClip(clip);
        showlistbutton.setOnAction(event -> {
            try
            {
                idc=course.getId_C();
                Parent root = FXMLLoader.load(getClass().getResource("/showWorkshopsF.fxml"));
                descCI.getScene().setRoot(root);

            }
            catch (IOException e)
            {
                System.out.println("Error loading FXML: " + e.getMessage());
            }
        });
        deletecoursebuttonF.setOnAction(event->{
            try
            {
                cs.delete(course.getId_C());
                showCoursesFController.refreshCoursesList();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Course deleted successfully ");
                alert.showAndWait();
            }
            catch (SQLException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
        editcoursebuttonF.setOnAction(event -> {
            try
            {
                i = course.getId_C();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateCourseF.fxml"));
                Parent root = loader.load();
                UpdateCourseFController updateCourseFController = loader.getController();
                updateCourseFController.setInstance(course,this);
                setUpdateCourseFController(updateCourseFController);
                descCI.getScene().setRoot(root);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });

    }


    public void updateDetails()
    {

    }
    private void deletecourse()
    {

    }


    @FXML
    void gotolistworkshops(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/showWorkshopsF.fxml"));
            descCI.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            e.printStackTrace();  // Add this line to print the exception details
        }
    }

    private void resetFields() {
    }


}
