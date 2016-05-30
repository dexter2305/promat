package com.poople.promat.adapters;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

public enum DTFormatter {

    INSTANCE;

    private static final String PATTERN_TIME = "h:m[:s]a";
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(PATTERN_TIME);
    private static final String PATTERN_DATE = "d/M/y";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(PATTERN_DATE);

    public DateTimeFormatter getTimeFormatter(){
        return timeFormatter;
    }

    public LocalTime getLocalTimeFrom(String timeAsString) {
        if (null == timeAsString || timeAsString.trim().isEmpty()) return null;
        LocalTime localTime = null;
        timeAsString = timeAsString.replaceAll(" ", "").toUpperCase();

        try {
            localTime = LocalTime.parse(timeAsString, timeFormatter);
        } catch (DateTimeParseException dtpe) {
        }
        return localTime;
    }

    public LocalDate getLocalDateFrom(Date date){
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        LocalDate localDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE));
        return localDate;
    }

    public LocalDate getLocalDateFrom(String valueAsString){
        if (valueAsString == null || valueAsString.trim().isEmpty())  return null;
        valueAsString = valueAsString.trim();
        valueAsString = valueAsString.replaceAll(" ", "");
        LocalDate localDate = LocalDate.parse(valueAsString, dateFormatter);
        return localDate;
    }
}
