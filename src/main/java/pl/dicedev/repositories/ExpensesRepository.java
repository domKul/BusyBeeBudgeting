package pl.dicedev.repositories;

import pl.dicedev.repositories.entities.ExpensesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpensesRepository extends JpaRepository<ExpensesEntity, UUID> {
}
