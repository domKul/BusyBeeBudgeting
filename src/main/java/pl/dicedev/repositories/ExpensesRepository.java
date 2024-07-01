package pl.dicedev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.dicedev.repositories.entities.ExpensesEntity;
import pl.dicedev.repositories.entities.UserEntity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ExpensesRepository extends JpaRepository<ExpensesEntity, UUID> {

    @Query("SELECT e FROM ExpensesEntity e WHERE e.userEntity = :userEntity AND e.purchaseDate >= :fromDate AND e.purchaseDate <= :toDate")
    List<ExpensesEntity> findAllByUserEntityAndDateBetween(UserEntity userEntity, Instant fromDate, Instant toDate);

}
