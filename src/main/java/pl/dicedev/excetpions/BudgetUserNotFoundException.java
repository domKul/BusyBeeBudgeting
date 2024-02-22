package pl.dicedev.excetpions;

import pl.dicedev.enums.AuthenticationMessageEnum;

public class BudgetUserNotFoundException extends RuntimeException {

    public BudgetUserNotFoundException() {
        super(AuthenticationMessageEnum.USER_NOT_FOUND.getMessage());
    }
}
