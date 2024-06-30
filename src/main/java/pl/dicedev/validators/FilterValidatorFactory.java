package pl.dicedev.validators;

public class FilterValidatorFactory {

    public static FilterParametersValidator getValidator(String filterType){
        if(filterType.equalsIgnoreCase("expenses")){
            return new ExpensesFilterParametersValidator();
        }if(filterType.equalsIgnoreCase("assets")){
            return new AssetValidator();
        }
        throw new IllegalArgumentException("Unknown filter type: " + filterType);
    }
}
