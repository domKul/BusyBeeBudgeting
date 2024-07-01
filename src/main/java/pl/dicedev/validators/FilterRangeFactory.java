package pl.dicedev.validators;

import org.springframework.stereotype.Component;


@Component
public class FilterRangeFactory {

    private final ExpensesFilteredRange expensesFilterRange;
    private final AssetsFilterRange assetsFilterRange;

    public FilterRangeFactory(ExpensesFilteredRange expensesFilterRange, AssetsFilterRange assetsFilterRange) {
        this.expensesFilterRange = expensesFilterRange;
        this.assetsFilterRange = assetsFilterRange;
    }

    public FilteredRange getFilterRange(String filterType){
        if (filterType.equalsIgnoreCase("assets")){
            return assetsFilterRange;
        }
        if (filterType.equalsIgnoreCase("expenses")){
            return expensesFilterRange;
        }
        throw new IllegalArgumentException("Unknown filter type: " + filterType);
    }
}
