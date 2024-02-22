package pl.dicedev.excetpions;

import pl.dicedev.enums.AuthenticationMessageEnum;

public class BudgetUserAlreadyExistsInDatabaseException extends RuntimeException {

    public BudgetUserAlreadyExistsInDatabaseException() {
        super(AuthenticationMessageEnum.USER_ALREADY_EXISTS.getMessage());
    }
}
