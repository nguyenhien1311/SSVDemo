package com.example.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeUtil {
	public static final Date NOW = new Date();
	private static final String FORMAT_STRING= "dd/MM/yyyy";
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(FORMAT_STRING);  
	
	public static String fromDatetoString(Date date) {
		return FORMATTER.format(date);
	}
	
	public static Date fromStringtoDate(String date) throws ParseException {
		return FORMATTER.parse(date);
	}
}
