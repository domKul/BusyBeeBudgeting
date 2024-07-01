package pl.dicedev.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dicedev.mappers.ExpensesMapper;
import pl.dicedev.repositories.ExpensesRepository;
import pl.dicedev.repositories.entities.ExpensesEntity;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.ExpensesDto;
import pl.dicedev.validators.FilterRangeFactory;
import pl.dicedev.validators.FilteredRange;

import java.util.*;
import java.util.logging.Logger;

@Service
public class ExpensesService {

    private final ExpensesRepository expensesRepository;
    private final ExpensesMapper expensesMapper;
    private final UserLogInfoService userLogInfoService;
    private final FilteredRange expensesFilteredRange;
    private static final Logger logger = Logger.getLogger(ExpensesService.class.getName());

    public ExpensesService(ExpensesRepository expensesRepository,
                           ExpensesMapper expensesMapper,
                           UserLogInfoService userLogInfoService,
                           FilterRangeFactory expensesFilteredRange) {
        this.expensesRepository = expensesRepository;
        this.expensesMapper = expensesMapper;
        this.userLogInfoService = userLogInfoService;
        this.expensesFilteredRange = expensesFilteredRange.getFilterRange("expenses");
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

   public List<ExpensesDto>getFilteredExpenses(Map<String,String>filters){
       UserEntity loggedUserEntity = userLogInfoService.getLoggedUserEntity();
        return expensesFilteredRange.getAllByFilter(loggedUserEntity, filters);
   }
}
