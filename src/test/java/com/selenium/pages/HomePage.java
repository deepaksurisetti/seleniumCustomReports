package com.selenium.pages;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.selenium.customexceptions.CustomException;
import com.selenium.uimapper.ScreenOneUIMapper;
import com.selenium.utilities.CommonLibrary;
import com.selenium.utilities.RandomFunctions;

public class HomePage {

	public static final Logger logger = LogManager.getLogger(HomePage.class);

	/**
	 * 
	 * @param pageType
	 */
	public static void navigateToPage(String pageType) {
		try {
			switch (pageType.trim().toUpperCase()) {

			case "REGISTRATION":
				CommonLibrary.isExist(By.xpath("//li/a[contains(text(),'Register')]"), 10).click();
				break;

			case "LOGIN":
				CommonLibrary.isExist(By.xpath("//li/a[contains(text(),'Login')]"), 10).click();
				break;

			default:
				break;
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * 
	 */
	public static void enterUserInformation(String fieldType, String value) {
		try {

			switch (fieldType.trim().toUpperCase()) {

			case "GENDER":
				if (value.contains("Male"))
					CommonLibrary.setRadioButton(By.xpath("//span[@class='male']//input"));
				else
					CommonLibrary.setRadioButton(By.xpath("//span[@class='female']//input"));
				break;

			case "FIRSTNAME":
				CommonLibrary.isExist(By.xpath("//input[@id='FirstName']"), 10).sendKeys(value);
				break;

			case "LASTNAME":
				CommonLibrary.isExist(By.xpath("//input[@id='LastName']"), 10).sendKeys(value);
				break;

			case "DOB":
				Select date = new Select(CommonLibrary.isExist(By.xpath("//select[@name='DateOfBirthDay']"), 10));
				date.selectByVisibleText(value.substring(0, 2));

				Select month = new Select(CommonLibrary.isExist(By.xpath("//select[@name='DateOfBirthMonth']"), 10));
				month.selectByVisibleText(RandomFunctions.genRandomMonth());

				Select year = new Select(CommonLibrary.isExist(By.xpath("//select[@name='DateOfBirthYear']"), 10));
				year.selectByVisibleText(value.substring(3, 7));

				break;

			case "EMAIL":
				CommonLibrary.isExist(By.xpath("//input[@id='Email']"), 10).sendKeys(value);
				break;

			case "COMPANY":
				CommonLibrary.isExist(By.xpath("//input[@id='Company']"), 10).sendKeys(value);
				break;

			case "PASSWORD":
				CommonLibrary.isExist(By.xpath("//input[@id='Password']"), 10).sendKeys(value);
				CommonLibrary.isExist(By.xpath("//input[@id='ConfirmPassword']"), 10).sendKeys(value);

				break;

			default:
				break;
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param buttonType
	 */
	public static void performClick(String buttonType) {
		try {
			switch (buttonType.trim().toUpperCase()) {

			case "REGISTER":
				CommonLibrary.isExist(By.id("register-button"), 10).click();
				if (CommonLibrary
						.verifyPresenceBoolean(By.xpath("//div[@class='message-error validation-summary-errors']")))
					throw new CustomException("Error Message Appeared while registering the new user");
				break;

			case "LOGIN":
				CommonLibrary.isExist(By.xpath("//button[contains(text(),'Log in')]"), 10).click();
				break;

			default:
				break;
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param errorMessage
	 */
	public static void verifyErrorMessage(String errorMessage) {
		try {

			if (!errorMessage.equals(
					CommonLibrary.isExist(By.xpath("//span[@class='field-validation-error']//span"), 5).getText()))
				throw new CustomException("Error Message Not matched/Displayed");

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param message
	 */
	public static void verifyRegistrationMessage(String message) {
		try {

			if (!"Your registration completed"
					.equals(CommonLibrary.isExist(By.xpath("//div[@class='result']"), 5).getText()))
				throw new CustomException("Registration Completed Message Not matched/Displayed");

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * 
	 */
	public static void enterUserInformationWithOut(String fieldType) {
		try {

			CommonLibrary.sleep(5000);
			if (!fieldType.equalsIgnoreCase("firstname"))
				CommonLibrary.isExist(By.xpath("//input[@id='FirstName']"), 10)
						.sendKeys(RandomFunctions.genRandomFirstName());

			if (!fieldType.equalsIgnoreCase("lastname"))
				CommonLibrary.isExist(By.xpath("//input[@id='LastName']"), 10)
						.sendKeys(RandomFunctions.genRandomLastName());

			if (!fieldType.equalsIgnoreCase("email"))
				CommonLibrary.isExist(By.xpath("//input[@id='Email']"), 10)
						.sendKeys(RandomFunctions.genRandomFirstName() + "@test.com");

			String password = RandomFunctions.genRandomPassword();
			if (!fieldType.equalsIgnoreCase("password"))
				CommonLibrary.isExist(By.xpath("//input[@id='Password']"), 10).sendKeys(password);

			if (!fieldType.equalsIgnoreCase("confirmpassword"))
				CommonLibrary.isExist(By.xpath("//input[@id='ConfirmPassword']"), 10).sendKeys(password);

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

}
