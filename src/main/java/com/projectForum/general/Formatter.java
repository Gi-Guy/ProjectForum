package com.projectForum.general;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Formats LocalDateTime objects into a nice format of time and date. 
 */
public class Formatter {
	
	// Defining Date and time format
	public static final DateTimeFormatter TIME_DATE = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
	public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static final String toTimeDate(LocalDateTime ldt) {
		return ldt.format(TIME_DATE);
	}
	
	public static final String toDate(LocalDateTime ldt) {
		return ldt.format(DATE);
	}
}
