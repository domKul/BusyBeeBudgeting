package pl.dicedev.repositories;

import org.springframework.data.jpa.repository.Query;
import pl.dicedev.repositories.entities.ExpensesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ExpensesRepository extends JpaRepository<ExpensesEntity, UUID> {

    @Query("SELECT e FROM ExpensesEntity e WHERE e.purchaseDate >= :from AND e.purchaseDate <= :to")
    List<ExpensesEntity> findAllBetweenDate(Instant from, Instant to);
}
