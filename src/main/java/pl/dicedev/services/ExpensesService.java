package pl.dicedev.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dicedev.mappers.ExpensesMapper;
import pl.dicedev.repositories.ExpensesRepository;
import pl.dicedev.repositories.entities.ExpensesEntity;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.ExpensesDto;

import java.util.*;

@Service
public class ExpensesService {

    private final ExpensesRepository expensesRepository;
    private final ExpensesMapper expensesMapper;
    private final UserLogInfoService userLogInfoService;


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

}
