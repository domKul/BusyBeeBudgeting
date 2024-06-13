package pl.dicedev.enums;

public enum ExpensesExceptionErrorMessages {
    MISSING_FROM_KEY("Missing filter key: from"),
    MISSING_TO_KEY("Missing filter key: to"),
    MISSING_MONTH_KEY("Missing filter key: month"),
    MISSING_YEAR_KEY("Missing filter key: year");
     private final String message;

    ExpensesExceptionErrorMessages(String s) {
        this.message = s;
    }

    public String getMessage() {
        return message;
    }
}
