package main.java.com.proterra.utilities;

import java.util.Random;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties; 
public class TestAPIUtils {
	public static java.util.Properties prop=null;

	public static String getRandomValue(){
		Random random = new Random();
		int radomInt = random.nextInt(100000);
		return Integer.toString(radomInt);
	}


	public static Properties loadPropertiesFile(String propertiesFileName){
		prop = new Properties();
		try {
			System.out.println("props file path : "+System.getProperty("user.dir")+propertiesFileName);
			InputStream input = new 
					FileInputStream(System.getProperty("user.dir")+propertiesFileName);
			prop.load(input);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop;
	}
	public static String  getPropertyValue(Properties prop, String key){
		System.out.println(prop.getProperty(key));
		return prop.getProperty(key);
	}
}
