package pl.dicedev.excetpions;

public class MissingExpensesFilterException extends RuntimeException{
    String message;
    public MissingExpensesFilterException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
