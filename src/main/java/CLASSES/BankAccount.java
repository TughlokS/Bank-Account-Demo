package CLASSES;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

import DBSPT.DBConnect;
import ENUMS.TransactionType;
import INTERFACES.AccountServices;
import INTERFACES.AccountUtilities;

public class BankAccount {

	// user info
	private String accountNumber;
	private String lastName;
	private String firstName;
	private String userName;
	private LocalDate dateOfBirth;
	private String email;
	private String passwordHash;
	private Timestamp createdAt;
	private double checkingBalance;
	private double minCheckingBalance;
	private double maxCheckingWithdrawal;

	// address info
	private String streetAddress;
	private String streetAddress2;
	private String city;
	private String stateCode;
	private String zipCode;

	// transaction
	private Transaction recentTransaction;





	// constructor
	public BankAccount(String accountNumber, String lastName, String firstName, String userName, LocalDate dateOfBirth, String email, String passwordHash, Timestamp createdAt, double checkingBalance, String streetAddress, String streetAddress2, String city, String stateCode,
			String zipCode) {
		this.accountNumber = accountNumber;
		this.lastName = lastName;
		this.firstName = firstName;
		this.userName = userName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.passwordHash = passwordHash;
		this.createdAt = createdAt;
		this.checkingBalance = checkingBalance;
		this.minCheckingBalance = checkingBalance * 0.25; // 25% of checking balance
		this.maxCheckingWithdrawal = checkingBalance * 0.4; // 40% of checking balance
		this.streetAddress = streetAddress;
		this.streetAddress2 = streetAddress2;
		this.city = city;
		this.stateCode = stateCode;
		this.zipCode = zipCode;
	}

	// getters
	public String getAccountNumber() { return accountNumber; }
	public String getLastName() { return lastName; }
	public String getFirstName() { return firstName; }
	public String getUserName() { return userName; }
	public LocalDate getDateOfBirth() { return dateOfBirth; }
	public String getEmail() { return email; }
	public String getPasswordHash() { return passwordHash; }
	public Timestamp getCreatedAt() { return createdAt; }
	public double getCheckingBalance() { return checkingBalance; }
	public double getMinCheckingBalance() { return minCheckingBalance; }
	public double getMaxCheckingWithdrawal() { return maxCheckingWithdrawal; }
	public String getStreetAddress() { return streetAddress; }
	public String getStreetAddress2() { return streetAddress2; }
	public String getCity() { return city; }
	public String getStateCode() { return stateCode; }
	public String getZipCode() { return zipCode; }
	public Transaction getRecentTransaction() { return recentTransaction; }

	// setters
	public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public void setUserName(String userName) { this.userName = userName; }
	public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
	public void setEmail(String email) { this.email = email; }
	public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
	public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
	public void setCheckingBalance(double checkingBalance) { this.checkingBalance = checkingBalance; }
	public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
	public void setStreetAddress2(String streetAddress2) { this.streetAddress2 = streetAddress2; }
	public void setCity(String city) { this.city = city; }
	public void setStateCode(String stateCode) { this.stateCode = stateCode; }
	public void setZipCode(String zipCode) { this.zipCode = zipCode; }
	public void setRecentTransaction(Transaction recentTransaction) { this.recentTransaction = recentTransaction; }





	// public methods
	public void deposit(double amount) throws ClassNotFoundException, SQLException {
		if (amount > 0) {
			checkingBalance += amount;
			updateMinMaxBalance();
			AccountServices.updateCheckingBalance(accountNumber, checkingBalance);
			Transaction newTransaction = new Transaction(accountNumber, TransactionType.DEPOSIT, amount, new Timestamp(System.currentTimeMillis()));
			newTransaction.writeToDatabase();
		}
		else {
			throw new IllegalArgumentException("Amount must be greater than 0");
		}
	}

	public void withdraw(double amount) throws ClassNotFoundException, SQLException {
		if (amount > 0 && amount <= checkingBalance) {
			if (amount <= maxCheckingWithdrawal) {
				checkingBalance -= amount;
				updateMinMaxBalance();
				AccountServices.updateCheckingBalance(accountNumber, checkingBalance);
				Transaction newTransaction = new Transaction(accountNumber, TransactionType.WITHDRAW, amount, new Timestamp(System.currentTimeMillis()));
				newTransaction.writeToDatabase();
			} else {
				throw new IllegalArgumentException("Withdrawal exceeds maximum withdrawal amount");
			}
		}
		else {
			throw new IllegalArgumentException("Amount must be greater than 0");
		}
	}

	public void updateMinMaxBalance() {
		minCheckingBalance = checkingBalance * 0.25;
		maxCheckingWithdrawal = checkingBalance * 0.4;
	}

	public void updateAddress(String newStreetAddress, String newStreetAddress2, String newZipCode, String newCity, String newStateCode) throws ClassNotFoundException, SQLException {
		this.streetAddress = newStreetAddress;
		this.streetAddress2 = newStreetAddress2;
		this.zipCode = newZipCode;
		this.city = newCity;
		this.stateCode = newStateCode;

		AccountServices.updateAddress(accountNumber, newStreetAddress, newStreetAddress2, newZipCode, newCity, newStateCode);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Account Number: " + accountNumber + "\n");
		sb.append("Last Name: " + lastName + "\n");
		sb.append("First Name: " + firstName + "\n");
		sb.append("Username: " + userName + "\n");
		sb.append("Date of Birth: " + dateOfBirth + "\n");
		sb.append("Email: " + email + "\n");
		sb.append("Password Hash: " + passwordHash + "\n");
		sb.append("Created At: " + createdAt + "\n");
		sb.append("Checking Balance: " + checkingBalance + "\n");
		sb.append("Minimum Checking Balance: " + minCheckingBalance + "\n");
		sb.append("Maximum Checking Withdrawal: " + maxCheckingWithdrawal + "\n");
		sb.append("Street Address: " + streetAddress + "\n");
		sb.append("Street Address 2: " + streetAddress2 + "\n");
		sb.append("City: " + city + "\n");
		sb.append("State Code: " + stateCode + "\n");
		sb.append("Zip Code: " + zipCode + "\n");

		return sb.toString();
	}

	public String toSQL() {
		StringBuilder sb = new StringBuilder();

		// insert into account_registry
		sb.append("INSERT INTO account_registry (AccountNumber, LastName, FirstName, UserName, DOB, EmailAddress, PasswordHash, CreatedAt) VALUES\n");
		sb.append("('" + accountNumber + "', " );
		sb.append("'" + lastName + "', " );
		sb.append("'" + firstName + "', " );
		sb.append("'" + userName + "', " );
		sb.append("'" + dateOfBirth + "', " );
		sb.append("'" + email + "', " );
		sb.append("'" + passwordHash + "', " );
		sb.append("'" + createdAt + "'" );
		sb.append(");\n");

		// insert into account_checking
		sb.append("INSERT INTO account_checking (AccountNumber, CheckingBalance) VALUES\n");
		sb.append("('" + accountNumber + "', " );
		sb.append(checkingBalance);
		sb.append(");\n");

		// insert into account_savings
		sb.append("INSERT INTO address (AccountNumber, StreetAddress, StreetAddress2, City, StateCode, ZipCode) VALUES\n");
		sb.append("('" + accountNumber + "', " );
		sb.append("'" + streetAddress + "', " );
		if (streetAddress2 != null) {
			sb.append("'" + streetAddress2 + "', " );
		} else {
			sb.append("NULL, ");
		}
		sb.append("'" + city + "', " );
		sb.append("'" + stateCode + "', " );
		sb.append("'" + zipCode + "'" );
		sb.append(");\n");

		return sb.toString();
	}

	public void writeToSQLFile() throws IOException {
		String fileLoc = AccountUtilities.sqlPopLoc + "BankDBPOP.sql";

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileLoc, true))) {
			bw.write(toSQL());
			bw.newLine();

			System.out.println();
			System.out.println("File successfully written to: " + fileLoc);
			System.out.println();
		}
	}

	public void writeToDatabase() throws ClassNotFoundException, SQLException {
		String sql = toSQL();
		try {
			DBConnect.executeQuery(sql, "BankDB");
		} catch (SQLException e) {
			System.out.println();
			System.out.println("Error writing to database: " + e.getMessage());
			System.out.println();
		}
	}





	// helper methods
	// NONE

}
