package main.java.com.proterra.ExtentListeners;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import main.java.com.proterra.utilities.DriverManager;


public class ExtentManager {

	private static ExtentReports extent;




	public static ExtentReports createInstance(String fileName) {
		//		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
		ExtentSparkReporter htmlReporter= new ExtentSparkReporter(fileName);


		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setDocumentTitle(fileName);
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName(fileName);

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Automation Team", "Proterra Automation Team");
		extent.setSystemInfo("Organization", "Proterra ");
		extent.setSystemInfo("Build no", "NA");


		return extent;
	}


	public static String screenshotPath;
	public static String screenshotName;

	public static void captureScreenshot() {

		File scrFile = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);

		Date d = new Date();
		screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";

		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "//extent_report//" + screenshotName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	//TODO: Review this code and remove is not required
	public static void getCustomAnnotationDetails(ITestContext context, Class<Annotation> clazz) {

		List<ITestNGMethod> methodsWithCustomAnnotation = Arrays.stream(context.getAllTestMethods())
				.filter(
						iTestMethod -> iTestMethod
						.getConstructorOrMethod()
						.getMethod()
						.getAnnotation(clazz) != null)
				.collect(Collectors.toList());

	}

}

