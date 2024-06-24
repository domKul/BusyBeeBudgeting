package pl.dicedev.validators;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.dicedev.excetpions.MissingExpensesFilterException;

@ControllerAdvice
public class ExpensesHndler {

    @ExceptionHandler(MissingExpensesFilterException.class)
    ResponseEntity<String> asd (MissingExpensesFilterException ex){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }
}
