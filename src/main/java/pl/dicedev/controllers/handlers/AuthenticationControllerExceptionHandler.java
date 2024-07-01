package pl.dicedev.controllers.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.dicedev.controllers.handlers.dtos.ErrorMessage;
import pl.dicedev.excetpions.BudgetInvalidUsernameOrPasswordException;

@RestControllerAdvice
public class AuthenticationControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage usernameOrPasswordIncorrectExceptionHandler(BudgetInvalidUsernameOrPasswordException exception) {
        return ErrorMessage.ErrorMessageBuilder.anErrorMessage()
                .withErrorDescription(exception.getMessage())
                .withErrorCode(String.valueOf(HttpStatus.FORBIDDEN.value()))
                .build();
    }
}
