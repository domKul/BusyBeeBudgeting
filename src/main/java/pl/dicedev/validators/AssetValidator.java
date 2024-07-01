package pl.dicedev.validators;

import org.springframework.stereotype.Component;
import pl.dicedev.excetpions.AssetIncompleteException;
import pl.dicedev.services.dtos.AssetDto;

import java.util.Arrays;
import java.util.List;

@Component
public class AssetValidator extends FilterParametersValidator {

    private final List<Validator> validatorList = Arrays.asList(new AmountValidator(), new IncomeDateValidator());

    public void validate(AssetDto dto) {
        ValidatorMessage validatorMessage = new ValidatorMessage();

        for (Validator validator : validatorList) {
            validator.valid(dto, validatorMessage);
        }

        if (!validatorMessage.getMessage().isEmpty()) {
            throwException(validatorMessage.getMessage());
        }
    }

    @Override
    protected void throwException(String missingKey) {
        throw new AssetIncompleteException(missingKey, "");
    }
}
