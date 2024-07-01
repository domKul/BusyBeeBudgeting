package pl.dicedev.validators;

import pl.dicedev.enums.ValidatorsAssetEnum;
import pl.dicedev.services.dtos.AssetDto;

class IncomeDateValidator implements Validator {

    @Override
    public ValidatorMessage valid(AssetDto dto, ValidatorMessage validatorMessage) {
        if (dto.getIncomeDate() == null) {
            validatorMessage.setMessage(ValidatorsAssetEnum.NO_INCOME_DATE.getMessage());
            validatorMessage.setCode("INCOME_DATE_MISSING");
        }
        return validatorMessage;
    }
}
