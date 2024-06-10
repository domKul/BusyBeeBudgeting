package pl.dicedev.builders;

import pl.dicedev.enums.AssetCategory;
import pl.dicedev.enums.ExpensesCategory;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.ExpensesDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ExpensesDtoBuilder {

    private BigDecimal amount;
    private Instant incomeDate;
    private ExpensesCategory category;
    private UUID userId;


    public ExpensesDtoBuilder withAmount( BigDecimal amount){
        this.amount = amount;
        return this;
    }
    public ExpensesDtoBuilder withIncomeDate( Instant incomeDate){
        this.incomeDate = incomeDate;
        return this;
    }
    public ExpensesDtoBuilder withCategory( ExpensesCategory category){
        this.category = category;
        return this;
    }
    public ExpensesDtoBuilder withUserId( UUID userId){
        this.userId = userId;
        return this;
    }


    public ExpensesDto builder(){
        var dto = new ExpensesDto();
        dto.setAmount(this.amount);
        dto.setUserId(this.userId);
        dto.setPurchaseData(this.incomeDate);
        dto.setCategory(this.category);
        return dto;
    }




}
