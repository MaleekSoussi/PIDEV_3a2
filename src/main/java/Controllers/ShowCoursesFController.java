package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import Models.Courses;
import Services.CoursesService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ShowCoursesFController
{
    private CoursesService cs = new CoursesService();
    @FXML
    private StackPane stack;


    @FXML
    private GridPane coursesGrid;

    @FXML
    private ScrollPane coursesItems;

    @FXML
    private TextField searchf;
    private Controllers.ShowWorkshopsFController ShowWorkshopsFController;
    private Controllers.courseItemController courseItemController;

    @FXML
    void addcoursebuttonF(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/addcourseF.fxml"));
            coursesGrid.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }

    @FXML
    void initialize()
    {
        refreshCoursesList();
        coursesGrid.setHgap(30); // Horizontal gap between items
        coursesGrid.setVgap(30); // Vertical gap between items

        coursesItems.setContent(coursesGrid);
        coursesItems.setFitToWidth(true);
        // Set minimum height for each row
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(10.0); // Set the desired height

        coursesGrid.getRowConstraints().add(rowConstraints);
        refreshCoursesList();
    }
    public void refreshCoursesList()
    {
        try
        {
            List<Courses> courselist = cs.read();
            courseListF(courselist);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void searchcoursesF(KeyEvent event) throws IOException {
        String searchQuery = searchf.getText();

        if (searchQuery.isEmpty())
        {
            resetCourseList();
        } else
        {
            List<Courses> matchingConseils = cs.searchCouses(searchQuery);
            updateCoursesGrid(matchingConseils);
        }
    }

    void resetCourseList()
    {
        coursesGrid.getChildren().clear();
        try
        {
            List<Courses> initialCourses = cs.read(); // Replace with your method to get the initial list
            updateCoursesGrid(initialCourses);
        }
        catch (IOException | SQLException e)
        {
            e.printStackTrace();
        }
    }

    void updateCoursesGrid(List<Courses> coursesList) throws IOException {
        coursesGrid.getChildren().clear();

        int rowIndex = 0;
        int colIndex = 0;
        for (Courses courses : coursesList) {
            Node coursenode;
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/courseItem.fxml"));
                coursenode = loader.load();
                courseItemController itemController = loader.getController();

                itemController.setData(courses);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            coursesGrid.add(coursenode, colIndex, rowIndex);
            colIndex++;
            if (colIndex == 4)
            {
                colIndex = 0;
                rowIndex++;
            }
        }
        coursesItems.setContent(coursesGrid);
        coursesItems.setFitToWidth(true);
    }


    public void courseListF(List<Courses> coursesList)
    {
        coursesItems.setFitToWidth(true);
        coursesItems.setFitToHeight(true);

        coursesGrid.getChildren().clear();
        coursesGrid.getRowConstraints().clear(); // Clear existing row constraints

        int columnCount = 0;
        int rowCount = 0;

        for (Courses course : coursesList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/courseItem.fxml"));
                Node coursenode = loader.load();
                courseItemController itemController = loader.getController();
                itemController.setParent(this);
                itemController.setData(course);

                RowConstraints row = new RowConstraints();
                row.setPrefHeight(360);
                coursesGrid.getRowConstraints().add(row); // Add row constraint to the grid
                coursesGrid.add(coursenode, columnCount, rowCount);

                columnCount++;

                if (columnCount == 4) {
                    columnCount = 0;
                    rowCount++;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        coursesItems.setContent(coursesGrid);
        coursesItems.setPrefHeight(coursesGrid.getHeight());
    }



    @FXML
    void gotofOb(ActionEvent event)
    {

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
    void effect(MouseEvent event)
    {

    }

    @FXML
    void eventbutton(ActionEvent event) {

    }

    @FXML
    void homebutton(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/frontOrBack.fxml"));
            coursesItems.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }

    @FXML
    void searchbutton(ActionEvent event) {

    }

    @FXML
    void viewcartbutton(ActionEvent event) {

    }

}
