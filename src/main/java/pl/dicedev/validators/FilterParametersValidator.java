package pl.dicedev.validators;

import java.util.Map;

public abstract class FilterParametersValidator {

    public void assertFilter(Map<String, String> filters) {
        checkIfMonthExistsAndYearIsMissing(filters);
        checkIfYearExistsAndMonthIsMissing(filters);
        checkIfFromDateExistsToAndToDateIsMissing(filters);
        checkIfToDateExistsAndFromDateIsMissing(filters);
    }

    protected abstract void throwException(String missingKey);

    protected void checkIfMonthExistsAndYearIsMissing(Map<String, String> filters) {
        if (filters.containsKey("month") && !filters.containsKey("year")) {
            throwException("year");
        }
    }

    protected void checkIfYearExistsAndMonthIsMissing(Map<String, String> filters) {
        if (filters.containsKey("year") && !filters.containsKey("month")) {
            throwException("month");
        }
    }

    protected void checkIfFromDateExistsToAndToDateIsMissing(Map<String, String> filters) {
        if (filters.containsKey("from") && !filters.containsKey("to")) {
            throwException("to");
        }
    }

    protected void checkIfToDateExistsAndFromDateIsMissing(Map<String, String> filters) {
        if (filters.containsKey("to") && !filters.containsKey("from")) {
            throwException("from");
        }
    }
}
