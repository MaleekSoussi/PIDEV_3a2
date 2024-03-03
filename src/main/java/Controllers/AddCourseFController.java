package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import Models.Courses;
import Services.CoursesService;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import javafx.stage.FileChooser;

import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;
import nl.captcha.Captcha;


public class AddCourseFController
{
    private final CoursesService cs = new CoursesService();
    public static String imagePath;


    @FXML
    private ImageView captchaImageView;

    @FXML
    private TextField captchaInputField;

    @FXML
    private Label descempty;

    @FXML
    private TextField descriptioncF;

    @FXML
    private Label imageId;

    @FXML
    private ImageView imageView;

    @FXML
    private Label imageempty;

    @FXML
    private TextField namecF;

    @FXML
    private Label nameempty;

    @FXML
    private TextField pricecF;

    @FXML
    private Label priceempty;

    @FXML
    private TextField searchfw;

    @FXML
    private TextField typecF;

    @FXML
    private Label typeempty;

    @FXML
    private Button uploadButton;

    private String captchaText;

    private Image uploadedImage;

    private Captcha captcha;

    public Image getUploadedImage() {
        return uploadedImage;
    }
    private Boolean isValid = true;

    public void initialize()
    {
        setvisibility();
        uploadButton.setOnAction(event -> handleUploadButton());
        captcha = setCaptcha();
    }
    public void setvisibility()
    {
        nameempty.setVisible(false);
        descempty.setVisible(false);
        typeempty.setVisible(false);
        imageempty.setVisible(false);
        priceempty.setVisible(false);
        imageId.setVisible(false);
        imageView.setVisible(false);
    }

    public Captcha setCaptcha()
    {
        Captcha captchaV = new Captcha.Builder(250, 150)
                .addText()
                .addBackground()
                .addNoise()
                .addBorder()
                .build();

        System.out.println(captchaV.getImage());
        Image image = SwingFXUtils.toFXImage(captchaV.getImage(), null);
        captchaImageView.setImage(image);
        return captchaV;
    }

    private void handleUploadButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\xampp\\htdocs\\images"));

        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if (selectedFile != null) {
            imagePath = selectedFile.toURI().toString();
            imageId.setText(imagePath);

            uploadedImage = new Image(imagePath);
            imageView.setImage(uploadedImage);
            imageView.setVisible(true);
        }
    }
    @FXML
    void addcourseF(ActionEvent event)
    {
        try
        {
            setvisibility();
            String name = namecF.getText().trim();
            String desc = descriptioncF.getText().trim();
            float price = pricecF.getText().isEmpty() ? 0.0f : Float.parseFloat(pricecF.getText().trim());
            String type = typecF.getText().trim();
            String pathimage = imageId.getText().trim();
            String captchaImagView = captchaInputField.getText().trim();

            imageView.setVisible(true);
            if (name.isEmpty())
            {
                nameempty.setVisible(true);
                isValid = false;
            }
            if (desc.isEmpty())
            {
                descempty.setVisible(true);
                isValid = false;
            }
            if (price==0.0f)
            {
                priceempty.setVisible(true);
                isValid = false;
            }
            if (type.isEmpty())
            {
                typeempty.setVisible(true);
                isValid = false;
            }
            if (pathimage.isEmpty())
            {
                imageempty.setVisible(true);
                isValid = false;
            }
            if(!isValid)
            {
                return;
            }
            if (!captcha.isCorrect(captchaInputField.getText()))
            {
                captcha = setCaptcha();
                captchaInputField.setText("");
                namecF.setText("");
                descriptioncF.setText("");
                pricecF.setText("");
                typecF.setText("");
                imageId.setText("");
                imageView.setImage(null);
                return;
            }
                cs.create(new Courses(name,desc,price,type,imagePath));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("success");
                alert.setContentText("Course added successfully ");
                alert.showAndWait();
                namecF.setText("");
                descriptioncF.setText("");
                pricecF.setText("");
                typecF.setText("");
                imageId.setText("");
                //imageView.setImage(null);
        }
        catch(SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/showCoursesF.fxml"));
            namecF.getScene().setRoot(root);
        }catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
    }
    @FXML
    void returncF(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/showCoursesF.fxml"));
            namecF.getScene().setRoot(root);
        }catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
    }

    @FXML
    void aboutusbutton(ActionEvent event) {

    }

    @FXML
    void artworksbutton(ActionEvent event) {

    }

    @FXML
    void auctionbutton(ActionEvent event) {

    }

    @FXML
    void eventbutton(ActionEvent event) {

    }

    @FXML
    void gotofOb(ActionEvent event) {

    }

    @FXML
    void homebutton(ActionEvent event) {

    }

    @FXML
    void searchbuttonw(ActionEvent event) {

    }

    @FXML
    void viewcartbuttonw(ActionEvent event) {

    }
}
