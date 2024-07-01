package pl.dicedev.validators;

import org.springframework.stereotype.Component;
import pl.dicedev.enums.ExpensesExceptionErrorMessages;
import pl.dicedev.excetpions.MissingExpensesFilterException;
@Component
class ExpensesFilterParametersValidator extends FilterParametersValidator {

    @Override
    protected void throwException(String missingKey) {
        ExpensesExceptionErrorMessages errorMessage =
                ExpensesExceptionErrorMessages.valueOf("MISSING_" + missingKey.toUpperCase() + "_KEY");
        throw new MissingExpensesFilterException(errorMessage.getMessage());
    }
}
