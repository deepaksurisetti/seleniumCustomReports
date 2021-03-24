package com.selenium.pages;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.selenium.customexceptions.CustomException;
import com.selenium.uimapper.ScreenOneUIMapper;
import com.selenium.utilities.CommonLibrary;

public class Page {
	public static final Logger logger = LogManager.getLogger(Page.class);

	/**
	 * 
	 */
	public static void enterFieldsInScreenOne(String mobile, String lender, String amount) {
		try {
			CommonLibrary.isExist(ScreenOneUIMapper.Mobile_Number_Input, 10).sendKeys(mobile);
			CommonLibrary.updateValueInGenPropFile("mobile", mobile);
			WebElement amountInputWebElement = CommonLibrary.isExist(ScreenOneUIMapper.Amount_Input, 10);
			amountInputWebElement.clear();
			amountInputWebElement.sendKeys(amount);
			CommonLibrary.updateValueInGenPropFile("amount", amountInputWebElement.getAttribute("value"));
			Select lenderDropDownMenu = new Select(CommonLibrary.isExist(ScreenOneUIMapper.Lender_Drop_Down_Input, 10));
			lenderDropDownMenu.selectByVisibleText(lender);
			CommonLibrary.updateValueInGenPropFile("lender", lender);

			CommonLibrary.isExist(ScreenOneUIMapper.Continue_Button_ScreenOne, 5).click();
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param screenType
	 */
	public static void verifyUserScreen(String screenType) {
		try {
			switch (screenType.trim().toUpperCase()) {

			case "SCREENTWO":
				CommonLibrary.verifyPageTitle("InstaCred Cardless EMI");
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
	 * @param screenType
	 */
	public static void verifyStaticTextContent(String screenType) {

		try {
			switch (screenType.trim().toUpperCase()) {

			case "SCREENTWO":
				CommonLibrary.verifyText(ScreenOneUIMapper.Complete_EMI_Transaction_Text, "Complete EMI Transaction");
				CommonLibrary.verifyText(ScreenOneUIMapper.No_Card_Needed_Text, "No Card needed");
				CommonLibrary.verifyText(ScreenOneUIMapper.Enter_Mobile_Num_Text,
						"Enter mobile registered with your bank");
				break;

			case "SCREENTHREE":

				String mobileNumber = CommonLibrary.getValueFromGenPropFile("mobile");

				if (CommonLibrary.isExist(By.xpath("//div[contains(text(),'" + mobileNumber + "')]"), 10) != null)
					logger.info("Mobile Number Matched");
				else
					throw new CustomException("Mobile Number Doesn't Matches in Screen Three");

				if (CommonLibrary.getValueFromGenPropFile("amount")
						.equals(CommonLibrary.isExist(ScreenOneUIMapper.Purchase_Amount, 10).getAttribute("innerText")
								.substring(1).replace(",", "")))
					logger.info("Amount Matched");
				else
					throw new CustomException("Amount Doesn't Matches in Screen Three");

				if (CommonLibrary.isExist(ScreenOneUIMapper.Lender_Name_Text, 10).getAttribute("innerText")
						.contains(CommonLibrary.getValueFromGenPropFile("lender")))
					logger.info("Lender Name Matched");
				else
					throw new CustomException("Lender Name Doesn't Matches in Screen Three");
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

			if (!errorMessage.equals(CommonLibrary.isExist(ScreenOneUIMapper.Input_Error, 5).getText()))
				throw new CustomException("Error Message Not matched/Displayed");

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * 
	 */
	public static void enterFieldsInScreenTwo(String mobileNumber) {
		try {

			String amount = CommonLibrary.getValueFromGenPropFile("amount");
			if (!CommonLibrary.isExist(ScreenOneUIMapper.Submit_Button, 5).isEnabled() && (amount.contains(CommonLibrary
					.isExist(ScreenOneUIMapper.Purchase_Amount, 10).getText().substring(1).replace(",", "")))) {

				CommonLibrary.updateValueInGenPropFile("mobile", mobileNumber);
				CommonLibrary.isExist(ScreenOneUIMapper.User_Mobile, 5).sendKeys(mobileNumber);

			} else {
				throw new CustomException("Error Message Not matched/Displayed");
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * 
	 */
	public static void verifyCancelTransaction() {
		try {

			CommonLibrary.isExist(ScreenOneUIMapper.Cancel_Txn_Link, 5).click();
			if (CommonLibrary.isExist(By.xpath("//dialog//div/header/h2"), 5).getText().equals("Cancel transaction?")
					&& CommonLibrary.isExist(By.xpath("//dialog//div//section/div"), 5).getText()
							.equals("If you cancel the transaction, you will be taken back to the merchant website.")) {

				WebElement continueButton = CommonLibrary.isExist(By.xpath("//dialog//div//footer//button"), 5);
				continueButton.isEnabled();
				continueButton.click();

				if (CommonLibrary.verifyPresenceBoolean(By.xpath("//dialog//div//section/div")))
					throw new CustomException("Dialog is not Closed");

			} else {
				throw new CustomException("Errro In Cancel Txn Dialog Box");
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 *  
	 */
	public static void verifyEmiOptions() {
		try {
			for (int i = 0; i < 4; i++) {
				List<WebElement> emiRows = CommonLibrary.isExistList(
						By.xpath("//div[@id='emi-panel-" + i + "']//div[@class='monthlyInstallment__1IaNm']"), 10);
				String duration = CommonLibrary
						.isExist(By.xpath("//div[@id='emi-panel-" + i + "']//div[@class='duration__36tJk']"), 10)
						.getAttribute("innerText").replace("\n", "").replace("\r", "");
				int monthlyEmi = CommonLibrary.calculateEmi(CommonLibrary.getValueFromGenPropFile("amount"),
						duration.substring(0, 1));
				int yearlyEmi = monthlyEmi * (Integer.parseInt(duration.substring(0, 1)));

//				if (String.valueOf(monthlyEmi).substring(0, 3)
//						.contains(emiRows.get(0).getAttribute("innerText").substring(1).replace(",", "")))
				logger.info("Monthly EMI from UI ->" + emiRows.get(0).getAttribute("innerText")
						+ "  | Monthly EMI from Calculation -> ₹" + String.valueOf(monthlyEmi));
//				else
//					throw new CustomException("Monthly EMI Values Doesn't Matched");

//				if (String.valueOf(yearlyEmi).substring(0, 3)
//						.contains(emiRows.get(1).getAttribute("innerText").substring(1).replace(",", "")))
				logger.info("Yearly EMI from UI ->" + emiRows.get(1).getAttribute("innerText")
						+ "  | Yearly EMI from Calculation -> ₹" + String.valueOf(yearlyEmi));
//				else
//					throw new CustomException("Yearly EMI Values Doesn't Matched");

				if (i == 0 && duration.equals("3months"))
					logger.info("Duration Matches for 3 Months");
				else if (i == 1 && duration.equals("6months"))
					logger.info("Duration Matches for 6 Months");
				else if (i == 2 && duration.equals("9months"))
					logger.info("Duration Matches for 9 Months");
				else if (i == 3 && duration.equals("12months"))
					logger.info("Duration Matches for 12 Months");

			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param emiDuration
	 */
	public static void chooseEMI(String emiDuration) {
		try {
			logger.info("EMI Duration is ->" + emiDuration);
			if (!CommonLibrary.isExist(By.id("submitButton"), 10).isEnabled()) {
				int rowStep = 0;
				if ("3".equals(emiDuration))
					rowStep = 0;
				else if ("6".equals(emiDuration))
					rowStep = 1;
				else if ("9".equals(emiDuration))
					rowStep = 2;
				else if ("12".equals(emiDuration))
					rowStep = 3;

				CommonLibrary.setRadioButton(By.xpath("//div[@id='emi-panel-" + rowStep + "']//input"));

				CommonLibrary.isExist(By.id("submitButton"), 10).click();
			} else {
				throw new CustomException("Continue Button is enabled even before selecting EMI Option");
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

}
