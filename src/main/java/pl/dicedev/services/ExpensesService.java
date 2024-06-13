package pl.dicedev.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dicedev.enums.ExpensesExceptionErrorMessages;
import pl.dicedev.enums.FilterExpensesParametersEnum;
import pl.dicedev.enums.MonthsEnum;
import pl.dicedev.excetpions.MissingExpensesFilterException;
import pl.dicedev.mappers.ExpensesMapper;
import pl.dicedev.repositories.ExpensesRepository;
import pl.dicedev.repositories.entities.ExpensesEntity;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.ExpensesDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.logging.Logger;

@Service
public class ExpensesService {

    private final ExpensesRepository expensesRepository;
    private final ExpensesMapper expensesMapper;
    private final UserLogInfoService userLogInfoService;
    private static final Logger logger = Logger.getLogger(ExpensesService.class.getName());



    public ExpensesService(ExpensesRepository expensesRepository, ExpensesMapper expensesMapper, UserLogInfoService userLogInfoService) {
        this.expensesRepository = expensesRepository;
        this.expensesMapper = expensesMapper;
        this.userLogInfoService = userLogInfoService;
    }


    public void saveExpenses(ExpensesDto expensesDto) {
        UserEntity loggedUserEntity = userLogInfoService.getLoggedUserEntity();
        ExpensesEntity expensesEntity = expensesMapper.fromDtoToEntity(expensesDto);
        expensesEntity.setUserEntity(loggedUserEntity);
        expensesRepository.save(expensesEntity);
    }

    public void deleteExpenses(UUID id) {
        ExpensesEntity expensesEntity = expensesRepository.findById(id).orElseThrow();
        expensesRepository.delete(expensesEntity);

    }

    @Transactional
    public ExpensesDto updateExpenses(ExpensesDto expensesDto, UUID id) {
        if (Objects.isNull(expensesDto)) {
            throw new IllegalArgumentException("Client update data cannot be null");
        }
        return expensesRepository.findById(id)
                .map(expense -> {
                    Optional.ofNullable(expensesDto.getCategory())
                            .ifPresent(category -> {
                                if (!Objects.equals(category, expense.getCategory())) {
                                    expense.setCategory(category);
                                }
                            });
                    Optional.ofNullable(expensesDto.getAmount())
                            .ifPresent(amount -> {
                                if (!Objects.equals(amount, expense.getAmount())) {
                                    expense.setAmount(amount);
                                }
                            });
                    return expensesMapper.fromEntityToDto(expense);
                }).orElseThrow(() -> new NoSuchElementException("Expense not found with id: " + id));
    }

   public List<ExpensesDto> getAllExpenses(){
       List<ExpensesEntity> all = expensesRepository.findAll();
       if(all.isEmpty()){
           return Collections.emptyList();
       }
       return expensesMapper.fromEntitiesToDtos(all);
   }

    public List<ExpensesDto> getFilteredExpenses(Map<String, String> filters) {
        validateFilterKeys(filters);
        if (filters.containsKey(FilterExpensesParametersEnum.MONTH.getKey()) && filters.containsKey(FilterExpensesParametersEnum.YEAR.getKey())) {
            MonthsEnum month = MonthsEnum.valueOf(filters.get(FilterExpensesParametersEnum.MONTH.getKey()).toUpperCase());
            int year = Integer.parseInt(filters.get(FilterExpensesParametersEnum.YEAR.getKey()));
            return getAllExpensesForMonthInYear(month, year);
        } else if (filters.containsKey(FilterExpensesParametersEnum.FROM.getKey()) && filters.containsKey(FilterExpensesParametersEnum.TO.getKey())) {
            String from = filters.get(FilterExpensesParametersEnum.FROM.getKey());
            String to = filters.get(FilterExpensesParametersEnum.TO.getKey());
            return getAllExpensesBetweenDate(from, to);
        }
        return  Collections.emptyList();
    }

    private List<ExpensesDto> getAllExpensesForMonthInYear(MonthsEnum month, int year) {
        Instant from = LocalDate.parse(month.getFirstDayForYear(year)).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant to = LocalDate.parse(month.getLastDayForYear(year)).atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        List<ExpensesEntity> expensesEntities = expensesRepository.findAllBetweenDate(from, to);
        return expensesMapper.fromEntitiesToDtos(expensesEntities);
    }

    public List<ExpensesDto> getAllExpensesBetweenDate(String from, String to) {
        Instant fromDate = LocalDate.parse(from).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant toDate = LocalDate.parse(to).atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        List<ExpensesEntity> allBetweenDate = expensesRepository.findAllBetweenDate(fromDate, toDate);
        return expensesMapper.fromEntitiesToDtos(allBetweenDate);
    }

    private void validateFilterKeys(Map<String, String> filters) {
        EnumSet<FilterExpensesParametersEnum> requiredKeys = EnumSet.noneOf(FilterExpensesParametersEnum.class);

        if (filters.containsKey(FilterExpensesParametersEnum.MONTH.getKey())) {
            requiredKeys.add(FilterExpensesParametersEnum.YEAR);
        } else if (filters.containsKey(FilterExpensesParametersEnum.YEAR.getKey())) {
            requiredKeys.add(FilterExpensesParametersEnum.MONTH);
        } else if (filters.containsKey(FilterExpensesParametersEnum.FROM.getKey())) {
            requiredKeys.add(FilterExpensesParametersEnum.TO);
        } else if (filters.containsKey(FilterExpensesParametersEnum.TO.getKey())) {
            requiredKeys.add(FilterExpensesParametersEnum.FROM);
        }

        for (FilterExpensesParametersEnum key : requiredKeys) {
            if (!filters.containsKey(key.getKey())) {
                logAndThrowException(ExpensesExceptionErrorMessages.valueOf("MISSING_" + key.name() + "_KEY"));
            }
        }
    }

    private void logAndThrowException(ExpensesExceptionErrorMessages errorMessage) {
        logger.warning(errorMessage.getMessage());
        throw new MissingExpensesFilterException(errorMessage.getMessage());
    }

}
