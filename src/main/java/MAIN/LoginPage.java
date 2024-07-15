package MAIN;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import CLASSES.BankAccount;
import INTERFACES.AccountBuilder;
import INTERFACES.AccountServices;

public class LoginPage {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;





    @FXML
    public void initialize() {
        loginButton.setOnAction(e -> {
            try {
                onLoginButtonClicked();
            } catch (ClassNotFoundException | SQLException e1) {
                e1.printStackTrace();
            }
        });

        password.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    loginButton.fire();
                    break;
                default:
                    break;
            }
        });

        registerButton.setOnAction(e -> onRegisterButtonClicked());
    }





    public  void onLoginButtonClicked() throws ClassNotFoundException, SQLException {
        String usernameText = username.getText();
        String passwordText = password.getText();

        if (usernameText.isEmpty() || passwordText.isEmpty()) {
            showAlert("Fill in all fields", "Please fill in all the fields before logging in.");
            return;
        }

        if (!AccountServices.authenticateUser(usernameText, passwordText)) {
            showAlert("Incorrect username or password", "Incorrect username or password, Please try again.");
            return;
        }

        BankAccount account = AccountBuilder.buildAccountFromUsername(usernameText);
        if (account == null) {
            showAlert("Error", "Account not found.");
            return;
        } else {
            navigateToAccountPage(account);
        }
    }

    public void onRegisterButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registration-page.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    /* -------------------------- // Private Methods \\ ------------------------- */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToAccountPage(BankAccount account) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("account-page.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the username to it
            AccountPage controller = loader.getController();
            controller.setAccount(account);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
