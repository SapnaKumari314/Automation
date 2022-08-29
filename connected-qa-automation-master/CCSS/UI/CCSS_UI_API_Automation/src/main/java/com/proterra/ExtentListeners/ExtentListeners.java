package main.java.com.proterra.ExtentListeners;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;


public class ExtentListeners implements ITestListener {

	static Date d = new Date();
	//	public static String fileName = "Proterra_Extent_" + d.toString().replace(":", "_").replace(" ", "_") + ".html";
	public static String fileName = "Extent_Report_Proterra.html";
	private static ExtentReports extent = ExtentManager.createInstance(System.getProperty("user.dir")+File.separator+"extent_report"+File.separator+fileName);
	public static ThreadLocal<ExtentTest> testReport = new ThreadLocal<ExtentTest>();


	public void onTestStart(ITestResult result) {

		String testCaseName;

		if(result.getMethod().getDescription().isBlank() || result.getMethod().getDescription().isEmpty()) {
			testCaseName = result.getTestClass().getName()+"     @TestCase : "+result.getMethod().getMethodName();
		}else {
			testCaseName = result.getMethod().getDescription();
		}

		ExtentTest test = extent.createTest(testCaseName);
		testReport.set(test);
	}


	public void onTestSuccess(ITestResult result) {


		String methodName=result.getMethod().getMethodName();
		String logText="<b>"+"TEST CASE:- "+ methodName.toUpperCase()+ " PASSED"+"</b>";		
		Markup m=MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		testReport.get().pass(m);
	}


	public void onTestFailure(ITestResult result) {

		String exceptionMessage=Arrays.toString(result.getThrowable().getStackTrace());
		testReport.get().fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Exception Occured:Click to see"
				+ "</font>" + "</b >" + "</summary>" +exceptionMessage.replaceAll(",", "<br>")+"</details>"+" \n");

		//		try {
		//			if (!(System.getProperty("test.Type").equalsIgnoreCase("API"))) {
		//				ExtentManager.captureScreenshot();
		//
		//
		//				testReport.get().fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>",
		//						MediaEntityBuilder.createScreenCaptureFromPath(ExtentManager.screenshotName)
		//						.build());
		//			}
		//		} catch (IOException e) {
		//
		//		}

		String failureLogg="TEST CASE FAILED";
		Markup m = MarkupHelper.createLabel(failureLogg, ExtentColor.RED);
		testReport.get().log(Status.FAIL, m);

	}


	public void onTestSkipped(ITestResult result) {
		String methodName=result.getMethod().getMethodName();
		String logText="<b>"+"Test Case:- "+ methodName+ " Skipped"+"</b>";		
		Markup m=MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
		testReport.get().skip(m);

	}


	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(ITestContext context) {

	}

	public void onFinish(ITestContext context) {

		if (extent != null) {

			extent.flush();
		}

	}


}
