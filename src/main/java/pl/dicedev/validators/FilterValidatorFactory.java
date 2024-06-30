package pl.dicedev.validators;

public class FilterValidatorFactory {

    public static FilterParametersValidator getValidator(String filterType){
        if(filterType.equalsIgnoreCase("expenses")){
            return new ExpensesFilterParametersValidator();
        }
        throw new IllegalArgumentException("Unknown filter type: " + filterType);
    }
}
