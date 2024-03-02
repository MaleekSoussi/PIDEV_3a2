package Controllers.Courses;

import Models.Courses;
import Services.CoursesandWorkshops.CoursesService;
import Services.User.UserService;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

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

    public Image getUploadedImage() {
        return uploadedImage;
    }
    private Boolean isValid = true;

    public void initialize()
    {
        setvisibility();
        uploadButton.setOnAction(event -> handleUploadButton());
        generateCaptcha();
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

    private void handleUploadButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\xampp\\htdocs\\image"));

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
            int Userid= UserService.currentlyLoggedInUser.getUserID();
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
            if (!captchaInputField.getText().equalsIgnoreCase(captchaText))
            {
                generateCaptcha();
                namecF.setText("");
                descriptioncF.setText("");
                pricecF.setText("");
                typecF.setText("");
                imageId.setText("");
                imageView.setImage(null);
                return;
            }
                cs.create(new Courses(name,desc,price,type,imagePath,Userid));
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
            Parent root = FXMLLoader.load(getClass().getResource("/Courses/showCoursesF.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/Courses/showCoursesF.fxml"));
            namecF.getScene().setRoot(root);
        }catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }
    }


    private void generateCaptcha()
    {
        captchaText = generateRandomText(6);
        Image captchaImage = createCaptchaImage(captchaText);
        captchaImageView.setImage(captchaImage);
    }

    private String generateRandomText(int length)
    {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++)
        {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }


    private Image createCaptchaImage(String text)
    {
        int width = 200;
        int height = 70;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // Set rendering hints for quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        // Custom background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Add complex background noise and shapes
        drawComplexBackground(g2d, width, height);

        // Randomize the font and color for each character
        Random random = new Random();
        Font[] fonts = {new Font("Serif", Font.BOLD, 28), new Font("SansSerif", Font.BOLD, 28),
                new Font("Monospaced", Font.BOLD, 28), new Font("Dialog", Font.BOLD, 28)};

        // Starting x and y coordinates
        int x = 10;
        int y = (height / 2) + 10;

        // Draw each character with different fonts and colors
        for (char c : text.toCharArray()) {
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g2d.setFont(fonts[random.nextInt(fonts.length)]);

            // Apply transformations for rotation and translation
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians(random.nextInt(25) - 12), 0, 0); // Rotate around the origin
            affineTransform.translate(x, y); // Translate to position the character
            g2d.setTransform(affineTransform);

            g2d.drawString(String.valueOf(c), 0, 0); // Draw character at the origin
            x += 30 + random.nextInt(5); // Increment x for the next character
        }

        // Overlay the text with more noise
        drawForegroundNoise(g2d, width, height);

        g2d.dispose();

        // Convert the BufferedImage to a JavaFX Image
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    private void drawComplexBackground(Graphics2D g2d, int width, int height) {
        Random random = new Random();
        // Draw random lines
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(width);
            int x2 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int y2 = random.nextInt(height);
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g2d.drawLine(x1, y1, x2, y2);
        }
        // Draw random circles
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int radius = random.nextInt(height / 4);
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 50)); // Semi-transparent
            g2d.fillOval(x, y, radius, radius);
        }
        // Draw random bezier curves
        for (int i = 0; i < 3; i++)
        {
            int x1 = random.nextInt(width), y1 = random.nextInt(height);
            int x2 = random.nextInt(width), y2 = random.nextInt(height);
            int ctrlX1 = random.nextInt(width), ctrlY1 = random.nextInt(height);
            int ctrlX2 = random.nextInt(width), ctrlY2 = random.nextInt(height);
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g2d.setStroke(new BasicStroke(1.5f + random.nextFloat())); // Variable stroke width
            g2d.draw(new CubicCurve2D.Float(x1, y1, ctrlX1, ctrlY1, ctrlX2, ctrlY2, x2, y2));
        }
    }

    private void drawForegroundNoise(Graphics2D g2d, int width, int height) {
        Random random = new Random();
        // Draw foreground noise, such as random lines or squiggles over the text
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i < 2; i++) {
            int xs = random.nextInt(width), ys = random.nextInt(height);
            int xe = xs + random.nextInt(width / 4), ye = ys + random.nextInt(height / 4);
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256), 100)); // Semi-transparent
            g2d.drawLine(xs, ys, xe, ye);
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
