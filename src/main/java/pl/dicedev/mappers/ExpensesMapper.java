package pl.dicedev.mappers;

import org.springframework.stereotype.Component;
import pl.dicedev.builders.ExpensesDtoBuilder;
import pl.dicedev.builders.ExpensesEntityBuilder;
import pl.dicedev.repositories.entities.ExpensesEntity;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.ExpensesDto;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class ExpensesMapper {

   public ExpensesDto fromEntityToDto(ExpensesEntity expensesEntity ){
        return new ExpensesDtoBuilder()
                .withUserId(expensesEntity.getId())
                .withAmount(expensesEntity.getAmount())
                .withCategory(expensesEntity.getCategory())
                .withIncomeDate(expensesEntity.getPurchaseDate())
                .withUserId(expensesEntity.getUserEntity().getId())
                .builder();
    }


    public ExpensesEntity fromDtoToEntity(ExpensesDto expensesDto ){
       return new ExpensesEntityBuilder()
               .withId(expensesDto.getId())
               .withAmount(expensesDto.getAmount())
               .withCategory(expensesDto.getCategory())
               .withIncomeDate(expensesDto.getPurchaseData())
               .builder();
    }


    public List<ExpensesDto> fromEntitiesToDtos(List<ExpensesEntity> expensesEntityList){
        return expensesEntityList.stream()
                .map(this::fromEntityToDto)
                .collect(Collectors.toList());
    }
}

