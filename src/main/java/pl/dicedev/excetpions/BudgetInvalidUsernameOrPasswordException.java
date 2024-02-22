package pl.dicedev.excetpions;

import pl.dicedev.enums.AuthenticationMessageEnum;

public class BudgetInvalidUsernameOrPasswordException extends RuntimeException {

    public BudgetInvalidUsernameOrPasswordException() {
        super(AuthenticationMessageEnum.INVALID_USERNAME_OR_PASSWORD.getMessage());
    }
}
