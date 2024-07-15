package INTERFACES;

import java.sql.ResultSet;
import java.sql.SQLException;

import DBSPT.DBConnect;

public interface AccountServices {

	// Method to update the checking balance in the database
	public static void updateCheckingBalance(String accountNumber, double newBalance) throws ClassNotFoundException, SQLException {
		String sql = "UPDATE account_checking SET CheckingBalance = " + newBalance + " WHERE AccountNumber = '" + accountNumber + "'";
		DBConnect.executeQuery(sql, "BankDB");
	}

	// Method to update the address in the database
	public static void updateAddress(String accountNumber, String streetAddress, String streetAddress2, String zipCode, String city, String stateCode) throws ClassNotFoundException, SQLException {
		String sql = "UPDATE address SET " +
				"StreetAddress = '" + streetAddress + "', " +
				"StreetAddress2 = '" + streetAddress2 + "', " +
				"ZipCode = '" + zipCode + "', " +
				"City = '" + city + "', " +
				"StateCode = '" + stateCode + "' " +
				"WHERE AccountNumber = '" + accountNumber + "'";
		DBConnect.executeQuery(sql, "BankDB");
	}

	// Method to update multiple fields in account_registry
	public static void updateAccountRegistry(String accountNumber, String lastName, String firstName, String email, String passwordHash) throws ClassNotFoundException, SQLException {
		String sql = "UPDATE account_registry SET " +
				"LastName = '" + lastName + "', " +
				"FirstName = '" + firstName + "', " +
				"EmailAddress = '" + email + "', " +
				"PasswordHash = '" + passwordHash + "' " +
				"WHERE AccountNumber = '" + accountNumber + "'";
		DBConnect.executeQuery(sql, "BankDB");
	}

	// Method to delete data from account_registry table given the accountNumber
	public static void deleteFromAccountRegistry(String accountNumber) throws ClassNotFoundException, SQLException {
		String sql = "DELETE FROM account_registry WHERE AccountNumber = '" + accountNumber + "'";
		DBConnect.executeQuery(sql, "BankDB");
	}

	// Method to check if AccountNumber exists
	public static boolean doesAccountNumberExist(String accountNumber) throws ClassNotFoundException, SQLException {
		String sql = "SELECT COUNT(*) FROM account_registry WHERE AccountNumber = '" + accountNumber + "'";
		ResultSet rs = DBConnect.executeResultsQuery(sql, "BankDB");
		if (rs.next()) {
			return rs.getInt(1) > 0;
		}
		return false;
	}

	// Method to check if Username exists
	public static boolean doesUsernameExist(String username) throws ClassNotFoundException, SQLException {
		String sql = "SELECT COUNT(*) FROM account_registry WHERE UserName = '" + username + "'";
		ResultSet rs = DBConnect.executeResultsQuery(sql, "BankDB");
		if (rs.next()) {
			return rs.getInt(1) > 0;
		}
		return false;
	}

	// Method to authenticate username and password
	public static boolean authenticateUser(String username, String password) throws ClassNotFoundException, SQLException {
		String sql = "SELECT PasswordHash FROM account_registry WHERE UserName = '" + username + "'";
		ResultSet rs = DBConnect.executeResultsQuery(sql, "BankDB");
		if (rs.next()) {
			String storedPasswordHash = rs.getString("PasswordHash");
			return AccountUtilities.hashPassword(password).equals(storedPasswordHash);
		}
		return false;
	}

}
