package controllers;

import entities.Order;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.OrderService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ShowOrderController {
    private final OrderService cs = new OrderService();
    private ObservableList<Order> ol;

    @FXML
    private DatePicker dateSO;

    @FXML
    private TableColumn<Order, String> datecol;

    @FXML
    private TableColumn<Order, Integer> idOcol;

    @FXML
    private TableView<Order> orderstable;

    @FXML
    private TextField priceSO;

    @FXML
    private TableColumn<Order, Float> pricecol;


    @FXML
    private TableColumn<Order, Order> deleteCol;

    @FXML
    void initialize() {
        loadOrders();
        setupDeleteColumn();
    }

    private void loadOrders() {
        try {
            List<Order> orders = cs.read();
            ol = FXCollections.observableList(orders);
            orderstable.setItems(ol);
            idOcol.setCellValueFactory(new PropertyValueFactory<>("idO"));
            datecol.setCellValueFactory(new PropertyValueFactory<>("dateC"));
            pricecol.setCellValueFactory(new PropertyValueFactory<>("totalP"));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", null, "Cannot load orders: " + e.getMessage());
        }
    }

    private void setupDeleteColumn() {
        deleteCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteCol.setCellFactory(param -> new TableCell<Order, Order>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);

                if (order == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> {
                    try {
                        cs.delete(order.getIdO());
                        getTableView().getItems().remove(order);
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Deleted", "Order deleted successfully.");
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Error", "Cannot delete order: " + e.getMessage());
                    }
                });
            }
        });
    }

    @FXML
    private void refreshButton() {
        try {
            ObservableList<Order> observableList = FXCollections.observableList(cs.read());
            orderstable.setItems(observableList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", null, "Cannot refresh orders: " + e.getMessage());
        }
    }

    @FXML
    private void updateButtons(ActionEvent event) {
        Order order = orderstable.getSelectionModel().getSelectedItem();
        if (order != null) {
            try {
                LocalDate selectedDate = dateSO.getValue();
                if (selectedDate == null) {
                    showAlert(Alert.AlertType.WARNING, "Validation Error", "No Date Selected", "Please select a date.");
                    return;
                }
                order.setDateC(selectedDate.toString());

                try {
                    float price = Float.parseFloat(priceSO.getText());
                    order.setTotalP(price);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Price", "Please enter a valid price.");
                    return;
                }

                cs.update(order, order.getIdO());
                refreshButton();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", null, "Cannot update order: " + e.getMessage());
            }
            resetFields();
        }
    }

    @FXML
    private void resetFields() {
        dateSO.setValue(null);
        priceSO.clear();
    }

    @FXML
    void click(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Order selectedOrder = orderstable.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateOrder.fxml"));
                    Parent root = loader.load();

                    UpdateOrderController updateOrderController = loader.getController();
                    updateOrderController.setOrder(selectedOrder);

                    Stage stage = (Stage) orderstable.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Navigation Error", null, "Unable to load the UpdateOrder scene: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    void returnButton(ActionEvent event) {
        navigateTo("/AddOrder.fxml");
    }

    private void navigateTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            orderstable.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", null, "Error loading " + fxmlPath + ": " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }




}
