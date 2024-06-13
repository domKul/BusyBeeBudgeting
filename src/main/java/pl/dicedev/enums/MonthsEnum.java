package pl.dicedev.enums;

public enum MonthsEnum {
    JANUARY(1, 31),
    FEBRUARY(2, 28),
    MARCH(3, 31),
    APRIL(4, 30),
    MAY(5, 31),
    JUNE(6, 30),
    JULY(7, 31),
    AUGUST(8, 31),
    SEPTEMBER(9, 30),
    OCTOBER(10, 31),
    NOVEMBER(11, 30),
    DECEMBER(12, 31);

    private final int monthNumber;
    private final int days;

    MonthsEnum(int monthNumber, int days) {
        this.monthNumber = monthNumber;
        this.days = days;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public int getDays(int year) {
        if (this == FEBRUARY && isLeapYear(year)) {
            return 29;
        }
        return days;
    }

    private boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    }

    public String getFirstDayForYear(int year) {
        return String.format("%d-%02d-01", year, monthNumber);
    }

    public String getLastDayForYear(int year) {
        return String.format("%d-%02d-%02d", year, monthNumber, getDays(year));
    }
}
