package pl.dicedev.controllers.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.dicedev.controllers.handlers.dtos.ErrorMessage;
import pl.dicedev.excetpions.MissingExpensesFilterException;

@RestControllerAdvice
public class ExpensesControllerExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessage usernameOrPasswordIncorrectExceptionHandler(MissingExpensesFilterException exception) {
        return ErrorMessage.ErrorMessageBuilder.anErrorMessage()
                .withErrorDescription(exception.getMessage())
                .withErrorCode(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
                .build();
    }
}
