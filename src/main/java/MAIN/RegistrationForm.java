package MAIN;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import CLASSES.BankAccount;
import INTERFACES.AccountServices;
import INTERFACES.AccountUtilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrationForm {

    @FXML
    private TextField lastName;
    @FXML
    private TextField firstName;
    @FXML
    private TextField username;
    @FXML
    private DatePicker DOB;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private TextField email;
    @FXML
    private TextField streetAddress;
    @FXML
    private TextField streetAddress2;
    @FXML
    private TextField city;
    @FXML
    private ComboBox<String> stateCode;
    @FXML
    private TextField zipCode;
    @FXML
    private Button createButton;
    @FXML
    private Button loginButton;

    // list of state for stateCode ComboBox
    private static final ObservableList<String> states = FXCollections.observableArrayList(
            "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE",
            "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY",
            "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV",
            "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI",
            "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
    );





    @FXML
    public void initialize() {
        initializeStateComboBox();
        createButton.setOnAction(e -> {
            try {
                onCreateButtonClicked();
            } catch (IOException | ClassNotFoundException | SQLException e1) {
                e1.printStackTrace();
            }
        });
    }





    public void onCreateButtonClicked() throws IOException, ClassNotFoundException, SQLException {
        if(checkAnyEmptyField()) {
            showAlert("Fill in all fields", "Please fill in all the fields before creating an account.");
            lastName.requestFocus();
        }
        else if (AccountServices.doesUsernameExist(username.getText())) {
            showAlert("Username Taken", "The username you entered is already in use. Please try another one.");
            username.requestFocus();
        }
        else if (!password.getText().equals(confirmPassword.getText())) {
            showAlert("Passwords Do Not Match", "The passwords you entered do not match. Please try again.");
            confirmPassword.requestFocus();
        }
        else {
            BankAccount account = createAccount();
            account.writeToSQLFile();
            account.writeToDatabase();
            System.out.println(account.toString());

            showAlert("Account Created", "Account has been successfully created." +
                    "\nYour account number is: " + account.getAccountNumber() +
                    "\nYour Username is: " + account.getUserName() +
                    "\nPlease remember your username and password.");
            resetFields();
        }
    }

    public void onLoginButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-page.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    /* -------------------------- // Private Methods \\ ------------------------- */
    // helper methods
    private void initializeStateComboBox() {
        // Create a FilteredList wrapping the ObservableList of states
        FilteredList<String> filteredStates = new FilteredList<>(states, s -> true);

        // Bind the ComboBox items to the FilteredList
        stateCode.setItems(filteredStates);
        // stateCode.setEditable(true);
    }

    private void resetFields() {
        lastName.clear();
        firstName.clear();
        username.clear();
        DOB.getEditor().clear();
        password.clear();
        confirmPassword.clear();
        email.clear();
        streetAddress.clear();
        streetAddress2.clear();
        city.clear();
        stateCode.getSelectionModel().clearSelection();
        zipCode.clear();

        lastName.requestFocus();
    }

    private boolean checkAnyEmptyField() {
        return lastName.getText().isEmpty() ||
                firstName.getText().isEmpty() ||
                username.getText().isEmpty() ||
                DOB.getValue() == null ||
                password.getText().isEmpty() ||
                confirmPassword.getText().isEmpty() ||
                email.getText().isEmpty() ||
                streetAddress.getText().isEmpty() ||
                city.getText().isEmpty() ||
                stateCode.getValue() == null ||
                zipCode.getText().isEmpty();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private BankAccount createAccount() throws SQLException, ClassNotFoundException {
        String accountNumber;
        // generates an account number, and it keeps on generating if the account number already exists
        do {
            accountNumber = AccountUtilities.generateAccountNumber();
        } while (AccountServices.doesAccountNumberExist(accountNumber));

        String lastName = this.lastName.getText();
        String firstName = this.firstName.getText();
        String username = this.username.getText();
        LocalDate DOB = LocalDate.parse(this.DOB.getValue().toString());
        String email = this.email.getText();
        String passwordHash = AccountUtilities.hashPassword(this.password.getText());
        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
        double checkingBalance = AccountUtilities.STARTING_BALANCE;
        String streetAddress = this.streetAddress.getText();
        String streetAddress2 = this.streetAddress2.getText().isEmpty() ? null : this.streetAddress2.getText();
        String city = this.city.getText();
        String stateCode = this.stateCode.getValue();
        String zipCode = this.zipCode.getText();

        BankAccount account = new BankAccount(accountNumber, lastName, firstName, username, DOB, email, passwordHash, createdAt, checkingBalance, streetAddress, streetAddress2, city, stateCode, zipCode);

        return account;
    }

}
