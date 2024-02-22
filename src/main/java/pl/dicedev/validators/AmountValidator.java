package pl.dicedev.validators;

import pl.dicedev.enums.ValidatorsAssetEnum;
import pl.dicedev.services.dtos.AssetDto;

import java.util.Objects;

class AmountValidator implements Validator {

    private Validator next = new IncomeDateValidator();

    @Override
    public ValidatorMessage valid(AssetDto dto, ValidatorMessage validatorMessage) {
        if (Objects.isNull(dto.getAmount())) {
            validatorMessage.setMessage(ValidatorsAssetEnum.NO_AMOUNT.getMessage());
            validatorMessage.setCode("EE48A612F34E402AAFA0871F66BB8F51");
        }

        return next.valid(dto, validatorMessage);
    }
}
