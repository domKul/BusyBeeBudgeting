package pl.dicedev.enums;

public enum FilterParametersCalendarEnum {
    MONTH("month"),
    YEAR("year"),
    FROM("from"),
    TO("to");

    private final String key;

    FilterParametersCalendarEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}