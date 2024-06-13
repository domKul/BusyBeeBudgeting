package pl.dicedev.enums;

public enum FilterExpensesParametersEnum {
    MONTH("month"),
    YEAR("year"),
    FROM("from"),
    TO("to");

    private final String key;

    FilterExpensesParametersEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}