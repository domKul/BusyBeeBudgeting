package pl.dicedev.validators;

import pl.dicedev.enums.ValidatorsAssetEnum;
import pl.dicedev.services.dtos.AssetDto;

class AmountValidator implements Validator {
    @Override
    public ValidatorMessage valid(AssetDto dto, ValidatorMessage validatorMessage) {
        if (dto.getAmount() == null && !validatorMessage.getMessage().contains(ValidatorsAssetEnum.NO_AMOUNT.getMessage())) {
            validatorMessage.setMessage(ValidatorsAssetEnum.NO_AMOUNT.getMessage());
            validatorMessage.setCode("AMOUNT_MISSING");
        }
        return validatorMessage;
    }
}
