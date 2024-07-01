package pl.dicedev.validators;

import pl.dicedev.services.dtos.AssetDto;

 public interface Validator {

    ValidatorMessage valid(AssetDto dto, ValidatorMessage validatorMessage);

}
