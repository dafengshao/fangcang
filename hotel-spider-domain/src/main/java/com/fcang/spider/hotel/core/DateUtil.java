package com.fcang.spider.hotel.core;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
	private DateUtil() {
		
	}
	public static LocalDate dateToLocalDate(Date date) {
	    Instant instant = date.toInstant();
	    ZoneId zone = ZoneId.systemDefault();
	    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
	   return localDateTime.toLocalDate();
	    
	}
	public static LocalDateTime dateToLocalDateTime(Date date) {
	    Instant instant = date.toInstant();
	    ZoneId zone = ZoneId.systemDefault();
	   return  LocalDateTime.ofInstant(instant, zone);
	}
	
	public static Date localDateToUdate(LocalDate localDate) {
	    ZoneId zone = ZoneId.systemDefault();
	    Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
	    return Date.from(instant);
	}
	
	public static LocalDate plusDays(int days) {
		LocalDate today = LocalDate.now();
		return today.plusDays(days);
	}
	
	public static LocalDate plusDays(Date date,int days) {
		LocalDate today = dateToLocalDate(date);
		return today.plusDays(days);
	}
	
	public static Date plusDaysToDate(Date date,int days) {
		LocalDate today = dateToLocalDate(date);
		return localDateToUdate(today.plusDays(days));
	}
}
