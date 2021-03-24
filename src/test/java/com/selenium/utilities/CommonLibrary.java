package com.selenium.utilities;

import java.io.FileReader;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

import com.google.common.base.Function;
import com.selenium.customexceptions.CustomException;
import com.selenium.drivers.DriverConstants;
import com.selenium.drivers.DriverSetup;

/**
 * 
 * @author Deepak
 *
 */
public class CommonLibrary extends DriverSetup {

	public static final Logger logger = LogManager.getLogger(CommonLibrary.class);
	static int Mwait = 20;
	static String browsername = System.getProperty(DriverConstants.BROWSER);

	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static Properties readPropertFile(String filePath) {
		try {
			Properties properties = new Properties();
			properties.load(new FileReader(filePath));
			return properties;
		} catch (Exception e) {
			Assert.fail("Property File Read Error");
		}
		return null;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public static void updateValueInGenPropFile(String key, String value) {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(
					"src\\test\\resources\\PropertyFiles\\general.properties");
			config.setProperty(key, value);
			config.save();
		} catch (ConfigurationException e) {
			logger.fatal("ConfigurationException in Gen Prop File");
		} catch (Exception e) {
			logger.fatal("Error in Updating Gen Prop File");
		}
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public static String getValueFromGenPropFile(String key) {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(
					"src\\test\\resources\\PropertyFiles\\general.properties");
			return config.getProperty(key).toString();

		} catch (ConfigurationException e) {
			logger.fatal("ConfigurationException in Gen Prop File");
		} catch (Exception e) {
			logger.fatal("Error in Updating Gen Prop File");
		}
		return null;
	}

	/**
	 * This method kills the current running driver threads and also quits the
	 * driver
	 */
	public static void killAllDriverProcess() {

		try {
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
			Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");

			logger.info("Killed All Driver Process");
			driver.quit();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 
	 * @param by
	 * @param timeOut
	 * @return
	 */
	public static WebElement isExist(final By by, int timeOut) {

		try {
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut))
					.pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class)
					.ignoring(WebDriverException.class).ignoring(StaleElementReferenceException.class);
			return wait.until(new Function<WebDriver, WebElement>() {

				@Override
				public WebElement apply(WebDriver webDriver) {
					return driver.findElement(by);
				}
			});
		} catch (TimeoutException e) {
			return null;
		}

	}

	/**
	 * 
	 * @param by
	 * @param timeOut
	 * @return
	 */
	public static List<WebElement> isExistList(final By by, int timeOut) {
		try {

			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut))
					.pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class)
					.ignoring(WebDriverException.class).ignoring(StaleElementReferenceException.class);

			return wait.until(new Function<WebDriver, List<WebElement>>() {

				@Override
				public List<WebElement> apply(WebDriver webDriver) {

					return driver.findElements(by);
				}
			});

		} catch (TimeoutException e) {

			return null;
		}

	}

	/**
	 * 
	 * @param pageTitle
	 */
	public static void verifyPageTitle(String pageTitle) {
		try {
			if (!driver.getTitle().equals(pageTitle))
				throw new CustomException("Current Page is not " + pageTitle);

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param locator
	 * @param text
	 */
	public static void verifyText(By locator, String text) {
		try {
			if (!text.equals(isExist(locator, 10).getText()))
				throw new CustomException("Text Is  not matched ->" + text);

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String addCommaToString(String s) {

		String formatted = "";
		if (s.length() > 1) {
			formatted = s.substring(0, 1);
			s = s.substring(1);
		}

		while (s.length() > 3) {
			formatted += "," + s.substring(0, 2);
			s = s.substring(2);
		}
		return formatted + "," + s;
	}

	/**
	 * 
	 * @param by
	 * @return
	 */
	public static Boolean verifyPresenceBoolean(final By by) {
		try {
			if (isExist(by, 5).isDisplayed())
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;

		}

	}

	/**
	 * 
	 * @param principal
	 * @param time
	 * @return
	 */
	public static int calculateEmi(String principal, String time) {

		int principalAmt = Integer.parseInt(principal);
		int duration = Integer.parseInt(time);
		Float rate = (float) 20;
		rate = rate / (12 * 100);

		return (int) Math
				.round((principalAmt * rate * Math.pow(1 + rate, duration)) / (Math.pow(1 + rate, duration) - 1));
	}

	/**
	 * 
	 * @param by
	 * @return
	 */
	public static boolean setRadioButton(By by) {
		return setCheckBox(by);
	}

	/**
	 * 
	 * @param by
	 * @return
	 */
	public static boolean setCheckBox(By by) {
		boolean result = false;
		try {
			for (int i = 0; i < 4; i++) {
				if (!driver.findElement(by).isSelected())
					clickJS(by);
				if (driver.findElement(by).isSelected())
					break;
				else
					sleep(Mwait);
			}
			result = driver.findElement(by).isSelected();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			result = false;
		}
		return result;
	}

	/**
	 * 
	 * @param ele
	 */
	public static void clickJS(WebElement ele) {
		if (ele != null) {
			try {

				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", ele);
				sleep(Mwait);

			} catch (StaleElementReferenceException e) {

			} catch (Exception e) {

			}
		} else {
			throw new NullPointerException("Object is not identified");
		}

	}

	/**
	 * 
	 * @param by
	 */
	public static void clickJS(By by) {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", driver.findElement(by));
			sleep(Mwait);
		} catch (StaleElementReferenceException e) {
		} catch (Exception e) {
			throw new CustomException("Exception", e.toString());
		}

	}

	/**
	 * 
	 * @param count
	 */
	public static void sleep(int count) {

		for (int j = 0; j <= (count / 1000); j++) {

			if (browsername.contains("internet")) {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			} else {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 
	 * @param element
	 * @param value
	 * @return
	 */
	public static boolean selectByVisibleText(WebElement element, String value) {
		if (element != null) {
			boolean result = false;
			try {
				Select select = new Select(element);
				select.selectByVisibleText(value);
				result = true;
			} catch (Exception e) {
				result = false;
			}
			return result;
		} else {
			throw new NullPointerException("Object for the value " + value + " is not identified");
		}
	}

}