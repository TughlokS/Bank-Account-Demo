package CLASSES;

import java.sql.SQLException;
import java.sql.Timestamp;

import DBSPT.DBConnect;
import ENUMS.TransactionType;

public class Transaction {

	private int transactionID;
	private String accountNumber;
	private TransactionType transactionType;
	private double amount;
	private Timestamp transactionTime;





	public Transaction() { }

	public  Transaction(String accountNumber, TransactionType transactionType, double transactionAmount, Timestamp transactionDateTime) {
		this.accountNumber = accountNumber;
		this.transactionType = transactionType;
		this.amount = transactionAmount;
		this.transactionTime = transactionDateTime;
	}

	public Transaction(int transactionID, String accountNumber, TransactionType transactionType, double transactionAmount, Timestamp transactionDateTime) {
		this.transactionID = transactionID;
		this.accountNumber = accountNumber;
		this.transactionType = transactionType;
		this.amount = transactionAmount;
		this.transactionTime = transactionDateTime;
	}

	public int getTransactionID() { return transactionID; }
	public String getAccountNumber() { return accountNumber; }
	public TransactionType getTransactionType() { return transactionType; }
	public double getAmount() { return amount; }
	public Timestamp getTransactionTime() { return transactionTime; }

	public void setTransactionID(int transactionID) { this.transactionID = transactionID; }
	public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
	public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
	public void setAmount(double transactionAmount) { this.amount = transactionAmount; }
	public void setTransactionTime(Timestamp transactionDateTime) { this.transactionTime = transactionDateTime; }





	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Transaction ID: " + transactionID + "\n");
		sb.append("Account Number: " + accountNumber + "\n");
		sb.append("Transaction Type: " + transactionType + "\n");
		sb.append("Amount: " + amount + "\n");
		sb.append("Transaction Time: " + transactionTime + "\n");
		return sb.toString();
	}

	public String toSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO transactions (AccountNumber, TransactionType, Amount, TransactionTime) VALUES\n");
		sb.append("('" + accountNumber + "', " );
		sb.append("'" + transactionType + "', " );
		sb.append(amount);
		sb.append(",'" + transactionTime + "'" );
		sb.append(");\n");
		return sb.toString();
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

}
