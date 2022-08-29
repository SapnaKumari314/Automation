package main.java.com.proterra.AssertManager;

import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import main.java.com.proterra.ExtentListeners.ExtentListeners;

public class SoftAssertLogger extends SoftAssert{
	
	@Override
	public void onAssertSuccess(IAssert<?> assertCommand) {
		String suffix = String.format("Actual value [%s] and Expected value [%s] match",
				assertCommand.getExpected().toString(), assertCommand.getActual().toString());
		ExtentListeners.testReport.get().pass(assertCommand.getMessage() + " | PASSED | "+ suffix);
		System.out.println(assertCommand.getMessage() + " | PASSED | "+ suffix);
	}

	@Override
	public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
		String suffix =
				String.format(
						"Expected [%s] but found [%s]",
						assertCommand.getExpected().toString(), assertCommand.getActual().toString());
		ExtentListeners.testReport.get().fail(assertCommand.getMessage() + " | FAILED | " + suffix);
		System.err.println(assertCommand.getMessage() + " | FAILED | " + suffix);
	}

}
