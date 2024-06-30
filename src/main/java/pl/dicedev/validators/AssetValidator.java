package pl.dicedev.validators;

import pl.dicedev.excetpions.AssetIncompleteException;
import pl.dicedev.services.dtos.AssetDto;
import org.springframework.stereotype.Component;

@Component
public class AssetValidator extends FilterParametersValidator {

    private final Validator validator = new AmountValidator();

    public void validate(AssetDto dto) {
        var validatorMessage = validator.valid(dto, new ValidatorMessage());
        if (validatorMessage.getMessage().isEmpty()) {
            return;
        }
        throw new AssetIncompleteException(validatorMessage.getMessage(), validatorMessage.getCode());
    }

    @Override
    protected void throwException(String missingKey) {

    }
}
