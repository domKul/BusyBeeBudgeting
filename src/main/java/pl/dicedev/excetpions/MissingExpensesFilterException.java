package pl.dicedev.excetpions;

public class MissingExpensesFilterException extends RuntimeException{
    public MissingExpensesFilterException(String message) {
        super(message);
    }
}
