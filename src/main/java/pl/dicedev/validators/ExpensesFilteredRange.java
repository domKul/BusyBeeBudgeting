package pl.dicedev.validators;

import org.springframework.stereotype.Component;
import pl.dicedev.mappers.ExpensesMapper;
import pl.dicedev.repositories.ExpensesRepository;
import pl.dicedev.repositories.entities.ExpensesEntity;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.ExpensesDto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExpensesFilteredRange extends AbstractFilteredRange<ExpensesEntity, ExpensesDto> {

    private final ExpensesRepository expensesRepository;
    private final ExpensesMapper expensesMapper;

    public ExpensesFilteredRange(ExpensesRepository expensesRepository, ExpensesMapper expensesMapper) {
        super("expenses");
        this.expensesRepository = expensesRepository;
        this.expensesMapper = expensesMapper;
    }

    @Override
    protected List<ExpensesEntity> getAllByUserAndDateBetween(UserEntity user, Instant from, Instant to) {
        return expensesRepository.findAllByUserEntityAndDateBetween(user, from, to);
    }

    @Override
    protected List<ExpensesDto> mapEntitiesToDto(List<? extends ExpensesEntity> entities) {
        return entities.stream().map(expensesMapper::fromEntityToDto).collect(Collectors.toList());
    }
}
