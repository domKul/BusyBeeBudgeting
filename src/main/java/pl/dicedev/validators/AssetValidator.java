package pl.dicedev.validators;

import pl.dicedev.excetpions.AssetIncompleteException;
import pl.dicedev.services.dtos.AssetDto;
import org.springframework.stereotype.Component;

@Component
public class AssetValidator {

    private Validator validator = new AmountValidator();

    public void validate(AssetDto dto) {
        var validatorMessage = validator.valid(dto, new ValidatorMessage());
        if (validatorMessage.getMessage().isEmpty()) {
            return;
        }
        throw new AssetIncompleteException(validatorMessage.getMessage(), validatorMessage.getCode());
    }

}
