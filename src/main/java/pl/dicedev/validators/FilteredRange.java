package pl.dicedev.validators;

import pl.dicedev.repositories.entities.UserEntity;

import java.util.List;
import java.util.Map;

public abstract class FilteredRange<T, U> {

    protected final FilterParametersValidator parametersValidator;

    protected FilteredRange(String filterType) {
        this.parametersValidator = FilterValidatorFactory.getValidator(filterType);
    }

    public List<U> getAllByFilter(UserEntity user, Map<String, String> filters) {
        parametersValidator.assertFilter(filters);
        return mapEntitiesToDto(getAllEntityBetweenDate(user, filters));
    }

    protected abstract List<T> getAllEntityBetweenDate(UserEntity user, Map<String, String> filters);

    protected abstract List<U> mapEntitiesToDto(List<? extends T> entities);
}
