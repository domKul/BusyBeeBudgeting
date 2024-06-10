package pl.dicedev.services.dtos;

import pl.dicedev.enums.ExpensesCategory;
import pl.dicedev.repositories.entities.UserEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ExpensesDto {

    private UUID id;
    private BigDecimal amount;
    private Instant purchaseData;
    private ExpensesCategory category;
    public UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getPurchaseData() {
        return purchaseData;
    }

    public void setPurchaseData(Instant purchaseData) {
        this.purchaseData = purchaseData;
    }

    public ExpensesCategory getCategory() {
        return category;
    }

    public void setCategory(ExpensesCategory category) {
        this.category = category;
    }

}
