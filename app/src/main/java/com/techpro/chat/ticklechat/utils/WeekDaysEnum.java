package com.techpro.chat.ticklechat.utils;

public enum WeekDaysEnum {

    SUNDAY("Sunday"),
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday");

    String mWeekDayValue;

    WeekDaysEnum(String day) {
        mWeekDayValue = day;
    }

    public String getSelectedWeekDayValue() {
        return mWeekDayValue;
    }
}
