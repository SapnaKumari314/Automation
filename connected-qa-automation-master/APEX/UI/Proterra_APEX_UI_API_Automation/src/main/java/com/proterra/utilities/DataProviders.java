package main.java.com.proterra.utilities;

import java.lang.reflect.Method;
import main.java.com.proterra.utilities.*;
import org.testng.annotations.DataProvider;

public class DataProviders {
	
	
	
	@DataProvider(name="masterDP",parallel=true)
	public static Object[][] getDataSuite1(Method m) {
		Constants constants= new Constants();
		System.out.println(m.getName());
		
		ExcelReader excel = new ExcelReader(constants.SUITE1_XL_PATH);
		String testcase = m.getName();
		return DataUtil.getData(testcase, excel);
	
	}
	


}
