package pl.dicedev.integrations;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.dicedev.builders.ExpensesDtoBuilder;
import pl.dicedev.enums.ExpensesCategory;
import pl.dicedev.enums.ExpensesExceptionErrorMessages;
import pl.dicedev.excetpions.MissingExpensesFilterException;
import pl.dicedev.repositories.entities.ExpensesEntity;
import pl.dicedev.services.dtos.ExpensesDto;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class ExpensesServiceIntegrationTest extends InitIntegrationTestData {


    @Test
    void shouldSaveOneExpensesInToDatabase() {
        //Given
        initDatabaseByUser();
        ExpensesDto expensesDto = new ExpensesDtoBuilder()
                .withCategory(ExpensesCategory.FUN)
                .withIncomeDate(Instant.now())
                .withAmount(new BigDecimal("123.50"))
                .builder();
        List<ExpensesEntity> emptyListBeforeExpenseAdded = expensesRepository.findAll();

        //When
        expensesService.saveExpenses(expensesDto);

        List<ExpensesEntity> afterExpenseAdded = expensesRepository.findAll();

        //Then
        assertThat(emptyListBeforeExpenseAdded).isNotEqualTo(afterExpenseAdded);
        assertThat(afterExpenseAdded.size()).isEqualTo(1);

    }

    @Test
    void shouldDeleteExpensesFromDatabase() {
        //Given
        initDatabaseByUser();
        ExpensesDto expensesDto = new ExpensesDtoBuilder()
                .withCategory(ExpensesCategory.FUN)
                .withIncomeDate(Instant.now(Clock.fixed(Instant.parse("2024-06-23T00:00:00Z"), ZoneOffset.UTC)))
                .withAmount(new BigDecimal("1223.50"))
                .builder();

        expensesService.saveExpenses(expensesDto);
        List<ExpensesEntity> checkingBeforeDelete = expensesRepository.findAll();

        //When
        expensesService.deleteExpenses(checkingBeforeDelete.get(0).getId());
        List<ExpensesEntity> checkingAfterDelete = expensesRepository.findAll();

        //Then
        assertAll(
                () -> assertThat(checkingBeforeDelete.size()).isEqualTo(1),
                () -> assertThat(checkingAfterDelete.size()).isEqualTo(0)
        );

    }

    @Test
    void shouldUpdateExpensesInDatabase() {
        //Given
        initDatabaseByUser();
        ExpensesDto expensesDto = new ExpensesDtoBuilder()
                .withCategory(ExpensesCategory.FUN)
                .withIncomeDate(Instant.now(Clock.fixed(Instant.parse("2024-06-23T00:00:00Z"), ZoneOffset.UTC)))
                .withAmount(new BigDecimal("1223.50"))
                .builder();
        ExpensesDto expensesUpdate = new ExpensesDtoBuilder()
                .withCategory(ExpensesCategory.OTHERS)
                .builder();

        expensesService.saveExpenses(expensesDto);
        ExpensesEntity expensesEntity = expensesRepository.findAll().get(0);

        //When
        expensesService.updateExpenses(expensesUpdate,expensesEntity.getId());
    }

    @Test
    void shouldReturnAllExpensesSavedInDatabase() {
    }

    @Test
    void shouldReturnAllExpensesBetweenDates() {
        // Given
        initDatabaseByUser();
        ExpensesDto expensesDto1 = new ExpensesDtoBuilder()
                .withCategory(ExpensesCategory.FUN)
                .withIncomeDate(Instant.now(Clock.fixed(Instant.parse("2024-06-20T00:00:00Z"), ZoneOffset.UTC)))
                .withAmount(new BigDecimal("1223.50"))
                .builder();
        ExpensesDto expensesDto2 = new ExpensesDtoBuilder()
                .withCategory(ExpensesCategory.FUN)
                .withIncomeDate(Instant.now(Clock.fixed(Instant.parse("2024-06-23T00:00:00Z"), ZoneOffset.UTC)))
                .withAmount(new BigDecimal("1223.50"))
                .builder();

        expensesService.saveExpenses(expensesDto1);
        expensesService.saveExpenses(expensesDto2);

        // When
        Map<String, String> filters = new HashMap<>();
        filters.put("from", "2024-06-20");
        filters.put("to", "2024-06-22");
        List<ExpensesDto> allExpensesBetweenDate = expensesService.getFilteredExpenses(filters);

        // Then
        assertAll(
                () -> assertThat(allExpensesBetweenDate.size()).isEqualTo(1)
        );
    }

    @Test
    void shouldThrowExceptionWhenFilterKeyFromIsMissing() {
        // Given
        initDatabaseByUser();
        Map<String, String> filters = new HashMap<>();
        filters.put("to", "2024-06-22");

        MissingExpensesFilterException exception = assertThrows(MissingExpensesFilterException.class,
                () -> expensesService.getFilteredExpenses(filters));

        // Then
        String expectedMessage = "Missing filter key: from";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFilterKeyToIsMissing() {
        // Given
        initDatabaseByUser();

        // When
        Map<String, String> filters = new HashMap<>();
        filters.put("from", "2024-06-22");

        MissingExpensesFilterException exception = assertThrows(MissingExpensesFilterException.class,
                () -> expensesService.getFilteredExpenses(filters));

        // Then
        String expectedMessage = "Missing filter key: to";
        assertEquals(expectedMessage, exception.getMessage());
    }


}
