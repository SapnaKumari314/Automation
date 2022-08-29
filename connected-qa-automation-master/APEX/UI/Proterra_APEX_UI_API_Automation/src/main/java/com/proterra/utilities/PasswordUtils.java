package main.java.com.proterra.utilities;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class PasswordUtils {
	
	public static String base64Decode(String str) {
		
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(str);
			String decodedString = new String(decodedBytes,"utf-8");
			return decodedString;
	    } catch (UnsupportedEncodingException e) {
	         e.printStackTrace();
	    }
		return str;
	}
}
