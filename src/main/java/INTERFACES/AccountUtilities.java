package INTERFACES;

import java.security.MessageDigest;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public interface AccountUtilities {

	public static final String sqlPopLoc = "C:\\Users\\tughl\\Documents\\IdeaProjects\\AccountDataStuff\\src\\main\\java\\DBSPT\\";
	public static final double STARTING_BALANCE = 5000.00;

	public static String hashPassword(String password) {
		try {
			// create a message digest password using SHA-256
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// perform the hashing
			byte[] hashBytes = md.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : hashBytes) {
				sb.append(String.format("%02x", b));
			}

			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException("Error hashing password", e);
		}
	}

	public static boolean verifyPassword(String password, String hash) {
		return hashPassword(password).equals(hash);
	}

	public static String generateAccountNumber() {
		// generate a random account number 
		// all account number starts with capital A followed by random 4 digit number
		return "A" + (int)(Math.random() * 10) + (int)(Math.random() * 10) + (int)(Math.random() * 10) + (int)(Math.random() * 10);
	}

	public static String formatDate(LocalDate date) {
		String formattedDate = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + date.getDayOfMonth() + ", " + date.getYear();

		return formattedDate;
	}

	public static String formatCurrency(double amount) {
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
		String formattedAmount = currencyFormatter.format(amount);
		return formattedAmount;
	}

}
