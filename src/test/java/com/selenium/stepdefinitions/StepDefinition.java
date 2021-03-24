package com.selenium.stepdefinitions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.selenium.constants.Constants;
import com.selenium.customexceptions.CustomException;
import com.selenium.drivers.DriverConstants;
import com.selenium.drivers.DriverSetup;
import com.selenium.pages.HomePage;
import com.selenium.utilities.CommonLibrary;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

/**
 * 
 * @author Deepak
 *
 */
public class StepDefinition extends DriverSetup {
	// Logger Intialization
	private static final Logger logger = LogManager.getLogger(StepDefinition.class);

	private static String scenarioName = "";
	private static String environment = "";
	private static String previousScenario = "";

	public String getFailureMessage() {
		return CommonLibrary.getValueFromGenPropFile("failureMessage");
	}

	public void setFailureMessage(String failureMessage) {
		CommonLibrary.updateValueInGenPropFile("failureMessage", failureMessage);
	}

	public String getBrowser() {
		return CommonLibrary.getValueFromGenPropFile("Browser");
	}

	public void setBrowser(String browser) {
		CommonLibrary.updateValueInGenPropFile("Browser", browser);
	}

	@Before
	public void launchBrowser() {
		try {

			String browser = System.getProperty(DriverConstants.BROWSER);
			setBrowser(browser);
			if (browser.equalsIgnoreCase(DriverConstants.CHROME))
				DriverSetup.chromeSetup();
			// else if ladder blocks can be constructed here, if we intended to use multiple
			// browsers, for now we have considered chrome alone
			else
				logger.error("Invalid Browser Name has been provided");
		} catch (Exception e) {
			setFailureMessage(e.getMessage());
			logger.fatal(e.getMessage());
			Assert.fail(e.getMessage());
		}
	}

	@Given("Navigate to Home Page")
	public void navigate_to_Home_Page() {
		try {
			environment = System.getProperty(Constants.ENVIRONMENT);
			String baseUrl = "";

			// Switch cases can be increased if we have more no. of environments to Test
			switch (environment.trim().toUpperCase()) {
			case Constants.DEMO:
				baseUrl = Constants.URL;
				break;

			default:
				break;
			}

			logger.info("Launching URL ->" + baseUrl + " for the Environment ->" + environment);
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			driver.get(baseUrl);

		} catch (Exception e) {
			setFailureMessage(e.getMessage());
			logger.fatal(getFailureMessage());
			Assert.fail(e.getMessage());
		}
	}

	@When("Navigate the User to {string} Page")
	public void navigate_the_User_to_Page(String pageType) {
		try {
			HomePage.navigateToPage(pageType);
		} catch (Exception e) {
			setFailureMessage(e.getMessage());
			logger.fatal(getFailureMessage());
			Assert.fail(e.getMessage());
		}
	}

	@When("Enter the {string} Information as {string} in the Registration Page")
	public void enter_the_Information_in_the_Registration_Page(String fieldType, String value) {
		try {
			HomePage.enterUserInformation(fieldType, value);
		} catch (Exception e) {
			setFailureMessage(e.getMessage());
			logger.fatal(getFailureMessage());
			Assert.fail(e.getMessage());
		}
	}

	@When("Enter User Information Without {string} in the Registration Page")
	public void enter_user_info_without_in_the_Registration_Page(String fieldType) {
		try {
			HomePage.enterUserInformationWithOut(fieldType);
		} catch (Exception e) {
			setFailureMessage(e.getMessage());
			logger.fatal(getFailureMessage());
			Assert.fail(e.getMessage());
		}
	}

	@When("Perform Click on the {string} button")
	public void perform_Click_on_the_button(String buttonType) {
		try {
			HomePage.performClick(buttonType);
		} catch (Exception e) {
			setFailureMessage(e.getMessage());
			logger.fatal(getFailureMessage());
			Assert.fail(e.getMessage());
		}
	}

	@When("Verify Error message {string}")
	public void verify_error_message(String message) {
		try {
			HomePage.verifyErrorMessage(message);
		} catch (Exception e) {
			setFailureMessage(e.getMessage());
			logger.fatal(getFailureMessage());
			Assert.fail(e.getMessage());
		}
	}

	@When("Verify the Registration message {string}")
	public void verify_registration_message(String message) {
		try {
			HomePage.verifyRegistrationMessage(message);
		} catch (Exception e) {
			setFailureMessage(e.getMessage());
			logger.fatal(getFailureMessage());
			Assert.fail(e.getMessage());
		}
	}

	@After
	public void tearDown(Scenario scenario) throws CustomException {
		try {

			scenarioName = scenario.getName();
			environment = System.getProperty("Environment");
			File filePath = null;
			Boolean flag = false;

			Collection<String> tags = scenario.getSourceTagNames();
			for (String eachTag : tags) {
				if ((eachTag.contains("Regression")) || (eachTag.contains("Test"))) {
					flag = true;
					filePath = new File("src/test/resources/TextReports/" + environment + ".txt");
				}
			}

			if (scenario.isFailed()) {
				logger.error(scenario.getName());
				TakesScreenshot screenShot = ((TakesScreenshot) driver);
				byte[] imageInBytes = screenShot.getScreenshotAs(OutputType.BYTES);
				scenario.embed(imageInBytes, "image/png");
			}
			driver.close();
			if (flag) {
				addDetailsToReport(scenario.isFailed(), filePath);
			}

		} catch (Exception e) {
			logger.fatal(e.getMessage());
		}
	}

	/**
	 * 
	 * @param scenarioStatus
	 * @param filePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void addDetailsToReport(boolean scenarioStatus, File filePath) throws FileNotFoundException, IOException {

		try (FileOutputStream fileOutputStream = new FileOutputStream(filePath, true)) {
			String reportHeader = "| Scenario | Environment | Browser | Status | Failure Message |\r\n";

			String status = "";
			String failed = "| Failed |";
			String passed = "| Passed |";

			if (filePath.exists()) {
				boolean flag = filePath.createNewFile();
				if (!flag)
					logger.info("New File created for Report Generation");
			}

			if (filePath.length() == 0) {
				byte[] content = reportHeader.getBytes();
				fileOutputStream.write(content);
				fileOutputStream.flush();
				logger.info("Headers has been created for Report Generation");
			}

			if (scenarioName.equals(previousScenario))
				status = "|" + " " + "|" + environment.toUpperCase() + "|" + getBrowser().toUpperCase();
			else
				status = "|" + scenarioName + "|" + environment.toUpperCase() + "|" + getBrowser().toUpperCase();

			previousScenario = scenarioName;

			if (scenarioStatus) {
				status = status.concat(failed);

				if (getFailureMessage().length() > 250)
					setFailureMessage(getFailureMessage().substring(0, 250));

				status = status.concat(getFailureMessage() + "|\r\n");
			} else {
				status = status.concat(passed);
				setFailureMessage("Not Applicable");
				status = status.concat(getFailureMessage() + "|\r\n");
			}

			byte[] content = status.getBytes();
			fileOutputStream.write(content);
			fileOutputStream.flush();
			logger.info("Test Status has been captured in Report Sucessfully");
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			setFailureMessage("");
		}
	}

}
