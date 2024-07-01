package pl.dicedev.validators;

import pl.dicedev.enums.FilterParametersCalendarEnum;
import pl.dicedev.enums.MonthsEnum;
import pl.dicedev.repositories.entities.UserEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

abstract class AbstractFilteredRange<T, D> extends FilteredRange<T, D> {

    protected AbstractFilteredRange(String filterType) {
        super(filterType);
    }

    protected abstract List<T> getAllByUserAndDateBetween(UserEntity user, Instant from, Instant to);

    @Override
    protected List<T> getAllEntityBetweenDate(UserEntity user, Map<String, String> filters) {
        if (filters.containsKey(FilterParametersCalendarEnum.MONTH.getKey()) && filters.containsKey(FilterParametersCalendarEnum.YEAR.getKey())) {
            MonthsEnum month = MonthsEnum.valueOf(filters.get(FilterParametersCalendarEnum.MONTH.getKey()).toUpperCase());
            int year = Integer.parseInt(filters.get(FilterParametersCalendarEnum.YEAR.getKey()));
            return getAllForMonthInYear(user, month, year);
        } else if (filters.containsKey(FilterParametersCalendarEnum.FROM.getKey()) && filters.containsKey(FilterParametersCalendarEnum.TO.getKey())) {
            String from = filters.get(FilterParametersCalendarEnum.FROM.getKey());
            String to = filters.get(FilterParametersCalendarEnum.TO.getKey());
            return getAllBetweenDates(user, from, to);
        }
        return List.of();
    }

    private List<T> getAllForMonthInYear(UserEntity user, MonthsEnum month, int year) {
        Instant from = LocalDate.parse(month.getFirstDayForYear(year)).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant to = LocalDate.parse(month.getLastDayForYear(year)).atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        return getAllByUserAndDateBetween(user, from, to);
    }

    private List<T> getAllBetweenDates(UserEntity user, String from, String to) {
        Instant fromDate = LocalDate.parse(from).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant toDate = LocalDate.parse(to).atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        return getAllByUserAndDateBetween(user, fromDate, toDate);
    }
}
