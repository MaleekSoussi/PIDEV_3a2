package Controllers.front.User;

import Models.Users;
import Services.User.PasswordHasher;
import Services.User.UserService;
import Test.MainFX;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;


public class ForgetPasswordController {

    private static final String ACCOUNT_SID = "AC5399d4bf35ce3020db83efbe5929634d";
    private static final String AUTH_TOKEN = "5b4809581d465a137f925e571dfc2e0d";
    private static final String TWILIO_NUMBER = "+17163201382";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String FROM_EMAIL = "nxtpixel01@gmail.com";
    private static final String EMAIL_PASSWORD = "tngi xfph mxii uvgi";
    private String temporarypass;
    private final UserService us = new UserService();


    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private ComboBox<String> selectionBox;

    @FXML
    void SendButton(ActionEvent event) {
        String selectedMethod = selectionBox.getValue();
        if (selectedMethod == null || (selectedMethod.equals("Email") && emailField.getText().trim().isEmpty()) || (selectedMethod.equals("SMS") && phoneField.getText().trim().isEmpty())) {
            us.showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a method (Email or SMS) and enter your email or phone number.");
            return;
        }

        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        try {
            Users user = us.readUserByEmail(email);
            if (user != null) {
                String TP = generateOTP();
                temporarypass = TP;
                String hashedOtp = PasswordHasher.hashPassword(TP);
                us.updatePassword(user, hashedOtp);

                if (selectedMethod.equals("Email")) {
                    sendEmail(email, TP); // Send OTP via email
                } else {
                    sendSMSToUser(phone, TP); // Send OTP via SMS
                }
                us.showAlert(Alert.AlertType.INFORMATION, "Notification", "One-time password sent successfully.");
            } else {
                us.showAlert(Alert.AlertType.ERROR, "Error", "User not found.");
            }
        } catch (SQLException e) {
            us.showAlert(Alert.AlertType.ERROR, "Database Error", "There was a problem accessing the user data.");
            e.printStackTrace(); // Log this error
        } catch (MessagingException | UnsupportedEncodingException e) {
            us.showAlert(Alert.AlertType.ERROR, "Communication Error", "Failed to send one-time password.");
            e.printStackTrace(); // Log this error
        }
    }

    private String generateOTP() {
        Random random = new Random();
        int TP = 100000 + random.nextInt(900000);
        return String.valueOf(TP);
    }

    private void sendOTPtoUser(String toEmail, String TP) throws MessagingException, UnsupportedEncodingException {
        String subject = "Your Temporary Password ";
        sendEmail(toEmail, subject);
        sendSMSToUser("recipient_phone_number", TP);
    }

    private void sendSMSToUser(String recipientPhoneNumber, String TP) {
        Message message = Message.creator(
                        new PhoneNumber(recipientPhoneNumber), // To number
                        new PhoneNumber(TWILIO_NUMBER), // From number (Your Twilio number)
                        "Your temporary password is: " + TP)
                .create();

        System.out.println("Sent message ID: " + message.getSid());
    }

    private void sendEmail(String toEmail, String subject) throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        };
        Session session = Session.getInstance(props, auth);

        jakarta.mail.internet.MimeMessage message = new jakarta.mail.internet.MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL, "Mail"));
        message.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject(subject);

        // Set content to HTML
        message.setContent(getHtmlContent(temporarypass), "text/html; charset=utf-8");

        Transport.send(message);
    }

    private String getHtmlContent(String TP) {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/styles/email.html");
            if (inputStream == null) {
                throw new IOException("Failed to load email template. File not found.");
            }

            String htmlContent;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                htmlContent = reader.lines().collect(Collectors.joining("\n"));
            }

            htmlContent = htmlContent.replace("{TP}", TP);
            return htmlContent;
        } catch (IOException e) {
            // Log the error or display a message to the user
            throw new RuntimeException("Failed to load email template: " + e.getMessage(), e);
        }
    }



    @FXML
    void goback(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Transition.fxml");
    }

}
