package com.selenium.uimapper;

import org.openqa.selenium.By;

public class ScreenOneUIMapper {
	public static final By Mobile_Number_Input = By.xpath("//form[@id='myForm']//td//input[@id='CI']");

	public static final By Lender_Drop_Down_Input = By.xpath("//form[@id='myForm']//td//select[@id='bank_code_fm']");

	public static final By Continue_Button_ScreenOne = By.xpath("//button[contains(text(),'Continue')]");

	public static final By Complete_EMI_Transaction_Text = By.xpath("//div[contains(text(),'Complete')]");

	public static final By No_Card_Needed_Text = By.xpath("//div[contains(text(),'No Card')]");

	public static final By Enter_Mobile_Num_Text = By.xpath("//div[contains(text(),'Enter mobile')]");

	public static final By Input_Error = By.className("inputError");

	public static final By Submit_Button = By.xpath("//button[contains(text(),'Submit')]");

	public static final By Purchase_Amount = By.id("purchase-amount");

	public static final By User_Mobile = By.id("payment-enter-mobile");

	public static final By Amount_Input = By.xpath("//td//input[@id='AMT']");

	public static final By Cancel_Txn_Link = By.xpath("//div[contains(text(),'Cancel')]");

	public static final By Mobile_Number_Text = By.xpath(
			"//div[@class='paymentTopHeader__35_Bz']//div[@class=contains(text(),'mobileNumber__3shaM bold-text')]/div");

	public static final By Lender_Name_Text = By.xpath(
			"//div[@class='lenderHeaderContainer__2WJ-j']//div[@class='lenderHeaderName__1KzDl font16 bold-text']");

	public static final By Select_EMI_Text = By
			.xpath("//div[@class='paymentBody__1FVzI']//div[@class='transaction-header bold-text']");
	
	
	
	

}
