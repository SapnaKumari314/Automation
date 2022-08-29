package main.java.com.proterra.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {


	//Get System Date Time
	public String getSystemDateTime(String outputDateTimeFormat) {
		String dateTimeStamp = new SimpleDateFormat(outputDateTimeFormat).format(new Date());
		return dateTimeStamp;

	}
	
	
	//Convert Date time string in UTC format
	public Date convertStringToUTCDateTime(String dateTime) throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");  
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Date date = formatter.parse(dateTime);
		return date;
	}
	
	
	//Get Current Date time in UTC format
	public Date getCurrentUTCDateTime() {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");  
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Date date = new Date();
		return date;
	}
	
	
	//Convert String to Date
	public Date convertStringToDate(String inputDate, String inputFormat) throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat(inputFormat); 
		Date date = formatter.parse(inputDate);
		return date;
	}
	

}
