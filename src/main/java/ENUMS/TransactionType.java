package ENUMS;

public enum TransactionType {

    DEPOSIT,
    WITHDRAW;

    public static TransactionType fromString(String transactionType) {
        if (transactionType != null) {
            for (TransactionType type : TransactionType.values()) {
                if (transactionType.equalsIgnoreCase(type.name())) {
                    return type;
                }
            }
        }

        throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
    }

}
