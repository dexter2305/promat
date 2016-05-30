package com.poople.promat.adapters;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

import com.poople.promat.models.DataError;

public enum DTFormatter {

    INSTANCE;

    private static final String PATTERN_TIME = "h:m[:s][a]";
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(PATTERN_TIME);
    private static final DateTimeFormatter timeFormatterAlternate = DateTimeFormatter.ofPattern("h.m[.s][a]");
    private static final String PATTERN_DATE = "d/M/y";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(PATTERN_DATE);
    private static final DateTimeFormatter dateFormatterAlternate = DateTimeFormatter.ofPattern("d-M-y");
    public DateTimeFormatter getTimeFormatter(){
        return timeFormatter;
    }
    public DateTimeFormatter getDateFormatter(){
        return dateFormatter;
    }

    public LocalTime getLocalTimeFrom(String timeAsString) throws DataError {
        if (null == timeAsString || timeAsString.trim().isEmpty()) return null;
        LocalTime localTime = null;
        timeAsString = timeAsString.replaceAll(" ", "").toUpperCase();

        try {
            localTime = LocalTime.parse(timeAsString, timeFormatter);
        } catch (DateTimeParseException dtpe) {
        	try {
                localTime = LocalTime.parse(timeAsString, timeFormatterAlternate);
            } catch (DateTimeParseException dtpe2) {
            	throw new DataError("Invalid Time :"+ timeAsString);
            }
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

    public LocalDate getLocalDateFrom(String valueAsString) throws DataError{
        if (valueAsString == null || valueAsString.trim().isEmpty())  return null;
        valueAsString = valueAsString.trim();
        valueAsString = valueAsString.replaceAll(" ", "");
        LocalDate localDate = null;
        try {
        localDate = LocalDate.parse(valueAsString, dateFormatter);
	    } catch (DateTimeParseException dtpe) {
	    	try {
	    		localDate = LocalDate.parse(valueAsString, dateFormatterAlternate);
	        } catch (DateTimeParseException dtpe2) {
	        	throw new DataError("Invalid Date :"+ valueAsString);
	        }
	    }
        return localDate;
    }
}
