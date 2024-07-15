package INTERFACES;

import java.sql.*;
import java.time.LocalDate;

import CLASSES.BankAccount;
import CLASSES.Transaction;
import DBSPT.DBConnect;
import ENUMS.TransactionType;

public interface AccountBuilder {

	// get account from account number
	public static BankAccount buildAccountFromQuery(String accountNumber) {
		String query = "SELECT ar.AccountNumber, ar.UserName, ar.LastName, ar.FirstName, ar.DOB, ar.EmailAddress, ar.PasswordHash, ar.CreatedAt, " +
						"ac.CheckingBalance, " +
						"ad.StreetAddress, ad.StreetAddress2, ad.ZipCode, ad.City, ad.StateCode " +
						"FROM account_registry ar " +
						"JOIN account_checking ac ON ar.AccountNumber = ac.AccountNumber " +
						"JOIN address ad ON ar.AccountNumber = ad.AccountNumber " +
						"WHERE ar.AccountNumber = '" + accountNumber + "'";

		try {
			ResultSet rs = DBConnect.executeResultsQuery(query, "BankDB");
			if (rs.next()) {
				String lastName = rs.getString("LastName");
				String firstName = rs.getString("FirstName");
				String userName = rs.getString("UserName");
				LocalDate dob = rs.getDate("DOB").toLocalDate();
				String email = rs.getString("EmailAddress");
				String passwordHash = rs.getString("PasswordHash");
				Timestamp createdAt = rs.getTimestamp("CreatedAt");
				double checkingBalance = rs.getDouble("CheckingBalance");
				String streetAddress = rs.getString("StreetAddress");
				String streetAddress2 = rs.getString("StreetAddress2");
				String zipCode = rs.getString("ZipCode");
				String city = rs.getString("City");
				String state = rs.getString("StateCode");

                return new BankAccount(
					accountNumber, lastName, firstName, userName, dob, email, passwordHash, createdAt, checkingBalance,
					streetAddress, streetAddress2, city, state, zipCode
				);
			}
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Database error: " + e.getMessage());
		}

		return null;
	}

	// get account from username
	public static BankAccount buildAccountFromUsername(String username) {
		String query = "SELECT ar.AccountNumber, ar.UserName, ar.LastName, ar.FirstName, ar.DOB, ar.EmailAddress, ar.PasswordHash, ar.CreatedAt, " +
				"ac.CheckingBalance, " +
				"ad.StreetAddress, ad.StreetAddress2, ad.ZipCode, ad.City, ad.StateCode " +
				"FROM account_registry ar " +
				"JOIN account_checking ac ON ar.AccountNumber = ac.AccountNumber " +
				"JOIN address ad ON ar.AccountNumber = ad.AccountNumber " +
				"WHERE ar.UserName = '" + username + "'";

		try {
			ResultSet rs = DBConnect.executeResultsQuery(query, "BankDB");
			if (rs.next()) {
				String accountNumber = rs.getString("AccountNumber");
				String lastName = rs.getString("LastName");
				String firstName = rs.getString("FirstName");
				LocalDate dob = rs.getDate("DOB").toLocalDate();
				String email = rs.getString("EmailAddress");
				String passwordHash = rs.getString("PasswordHash");
				Timestamp createdAt = rs.getTimestamp("CreatedAt");
				double checkingBalance = rs.getDouble("CheckingBalance");
				String streetAddress = rs.getString("StreetAddress");
				String streetAddress2 = rs.getString("StreetAddress2");
				String zipCode = rs.getString("ZipCode");
				String city = rs.getString("City");
				String state = rs.getString("StateCode");

				BankAccount account = new BankAccount(
						accountNumber, lastName, firstName, username, dob, email, passwordHash, createdAt, checkingBalance,
						streetAddress, streetAddress2, city, state, zipCode
				);

				return account;
			}
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Database error: " + e.getMessage());
		}

		return null;
	}

	// Load the most recent transaction from the database
	public static Transaction loadMostRecentTransaction(String accountNumber) throws ClassNotFoundException, SQLException {
		String query = "SELECT * FROM transactions WHERE AccountNumber = '" + accountNumber + "' ORDER BY TransactionTime DESC LIMIT 1";
		ResultSet rs = DBConnect.executeResultsQuery(query, "BankDB");

		if (rs.next()) {
			int transactionID = rs.getInt("TransactionID");
			TransactionType transactionType = TransactionType.fromString(rs.getString("TransactionType"));
			double amount = rs.getDouble("Amount");
			Timestamp transactionTime = rs.getTimestamp("TransactionTime");

			return new Transaction(transactionID, accountNumber, transactionType, amount, transactionTime);
		}

		return null;
	}

}