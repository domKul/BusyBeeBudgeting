package pl.dicedev.services;

import org.springframework.stereotype.Component;
import pl.dicedev.enums.FilterExpensesParametersEnum;
import pl.dicedev.enums.MonthsEnum;
import pl.dicedev.mappers.ExpensesMapper;
import pl.dicedev.repositories.ExpensesRepository;
import pl.dicedev.repositories.entities.AssetEntity;
import pl.dicedev.repositories.entities.ExpensesEntity;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.ExpensesDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExpensesFilteredRange extends FilteredRange<ExpensesEntity, ExpensesDto> {

    private final ExpensesRepository expensesRepository;
    private final ExpensesMapper expensesMapper;


    public ExpensesFilteredRange(ExpensesRepository expensesRepository, ExpensesMapper expensesMapper) {
        super("expenses");
        this.expensesRepository = expensesRepository;
        this.expensesMapper = expensesMapper;
    }

    @Override
    protected List<ExpensesEntity> getAllEntityBetweenDate(UserEntity user, Map<String, String> filters) {
        if (filters.containsKey(FilterExpensesParametersEnum.MONTH.getKey()) && filters.containsKey(FilterExpensesParametersEnum.YEAR.getKey())) {
            MonthsEnum month = MonthsEnum.valueOf(filters.get(FilterExpensesParametersEnum.MONTH.getKey()).toUpperCase());
            int year = Integer.parseInt(filters.get(FilterExpensesParametersEnum.YEAR.getKey()));
            return getAllExpensesForMonthInYear(user, month, year);
        } else if (filters.containsKey(FilterExpensesParametersEnum.FROM.getKey()) && filters.containsKey(FilterExpensesParametersEnum.TO.getKey())) {
            String from = filters.get(FilterExpensesParametersEnum.FROM.getKey());
            String to = filters.get(FilterExpensesParametersEnum.TO.getKey());
            return getAllExpensesBetweenDate(user, from, to);
        }
        return Collections.emptyList();
    }

    @Override
    protected List<ExpensesDto> mapEntitiesToDto(List<? extends ExpensesEntity> entities) {
        return entities.stream().map(expensesMapper::fromEntityToDto).collect(Collectors.toList());
    }


    private List<ExpensesEntity> getAllExpensesForMonthInYear(UserEntity user, MonthsEnum month, int year) {
        Instant from = LocalDate.parse(month.getFirstDayForYear(year)).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant to = LocalDate.parse(month.getLastDayForYear(year)).atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        return expensesRepository.findAllByUserEntityAndDateBetween(user, from, to);
    }

    private List<ExpensesEntity> getAllExpensesBetweenDate(UserEntity user, String from, String to) {
        Instant fromDate = LocalDate.parse(from).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant toDate = LocalDate.parse(to).atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        return expensesRepository.findAllByUserEntityAndDateBetween( user, fromDate,toDate);
    }
}
