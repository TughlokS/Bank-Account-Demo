package MAIN;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import CLASSES.BankAccount;
import CLASSES.Transaction;
import ENUMS.TransactionType;
import INTERFACES.AccountBuilder;
import INTERFACES.AccountUtilities;

public class AccountPage {

    @FXML
    private Label nameHeader;
    @FXML
    private Label dateLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label transactionLabel;
    @FXML
    private Button returnButton;
    @FXML
    private Button withdrawButton;
    @FXML
    private Button depositButton;

    private BankAccount account;





    @FXML
    public void initialize() {
        returnButton.setOnAction(e -> onReturnButtonClicked());
        // Ensure handlers are set only once
        if (withdrawButton.getOnAction() == null) {
            withdrawButton.setOnAction(e -> handleWithdraw());
        }

        if (depositButton.getOnAction() == null) {
            depositButton.setOnAction(e -> handleDeposit());
        }
    }





    // setAccount method is called by the login page
    @SuppressWarnings("exports")
    public void setAccount(BankAccount account) {
        this.account = account;
        updateUI();
    }

    public void onReturnButtonClicked() {
        navigateToLoginPage();
    }





    /* -------------------------- // Private methods \\ ------------------------- */
    // first call is by the login page when logging in
    private void updateUI() {
        String fName = account.getFirstName();
        String lName = account.getLastName().trim().isEmpty() ? "" : " " + account.getLastName();
        String headerString = "Welcome, " + fName + lName + ".";
        String dateString = "Today is " + AccountUtilities.formatDate(LocalDate.now()) + ".";
        String balanceString = AccountUtilities.formatCurrency(account.getCheckingBalance());

        nameHeader.setText(headerString);
        dateLabel.setText(dateString);
        balanceLabel.setText(balanceString);
        handleTransactionLogic();
    }

    private void handleTransactionLogic() {
        try {
            Transaction recentTransaction = AccountBuilder.loadMostRecentTransaction(account.getAccountNumber());

            if (recentTransaction == null) {
                transactionLabel.setText("N/A");
                transactionLabel.setStyle("-fx-text-fill: #cacaca;");
            } else {
                String transactionString = recentTransaction.getTransactionType() == TransactionType.DEPOSIT ? "" : "-";
                transactionString += AccountUtilities.formatCurrency(recentTransaction.getAmount());
                transactionLabel.setText(transactionString);

                if (recentTransaction.getTransactionType() == TransactionType.DEPOSIT) {
                    transactionLabel.setStyle("-fx-text-fill: #13a100;");
                } else if (recentTransaction.getTransactionType() == TransactionType.WITHDRAW) {
                    transactionLabel.setStyle("-fx-text-fill: #d40000;");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-page.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) returnButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleWithdraw() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Withdraw");
        dialog.setHeaderText("Withdraw Money");
        dialog.setContentText("Please enter the amount to withdraw:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(amountString -> {
            try {
                double amount = Double.parseDouble(amountString);
                account.withdraw(amount);
                updateUI();
            } catch (NumberFormatException e) {
                showErrorAlert("Invalid amount", "Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                showErrorAlert("Invalid amount", e.getMessage());
            } catch (ClassNotFoundException | SQLException e) {
                showErrorAlert("Database Error", "Could not process the transaction.");
                e.printStackTrace();
            }
        });
    }

    private void handleDeposit() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Deposit");
        dialog.setHeaderText("Deposit Money");
        dialog.setContentText("Please enter the amount to deposit:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(amountString -> {
            try {
                double amount = Double.parseDouble(amountString);
                account.deposit(amount);
                updateUI();
            } catch (NumberFormatException e) {
                showErrorAlert("Invalid amount", "Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                showErrorAlert("Invalid amount", e.getMessage());
            } catch (ClassNotFoundException | SQLException e) {
                showErrorAlert("Database Error", "Could not process the transaction.");
                e.printStackTrace();
            }
        });
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
