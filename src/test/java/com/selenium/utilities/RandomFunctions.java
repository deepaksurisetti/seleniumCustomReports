package com.selenium.utilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RandomFunctions {

	/**
	 * 
	 * @return
	 */
	public static String genRandomFirstName() {
		List<String> firstName = Arrays.asList("Bob", "Jill", "Tom", "Brandon", "ABob", "AJill", "ATom", "ABrandon",
				"BobA", "JillA", "TomA", "BrandonA");

		Collections.shuffle(firstName);
		return firstName.get(0).trim();
	}

	/**
	 * 
	 * @return
	 */
	public static String genRandomLastName() {
		List<String> lastName = Arrays.asList("Bob", "Jill", "Tom", "Brandon", "ABob", "AJill", "ATom", "ABrandon",
				"BobA", "JillA", "TomA", "BrandonA");

		Collections.shuffle(lastName);
		return lastName.get(0).trim();
	}

	/**
	 * 
	 * @return
	 */
	public static String genRandomCompany() {
		List<String> company = Arrays.asList("TCS", "CTS", "WIPRO", "IBM", "Facebook", "Amazon");

		Collections.shuffle(company);
		return company.get(0).trim();
	}

	/**
	 * 
	 * @return
	 */
	public static String genRandomPassword() {
		List<String> password = Arrays.asList("Cooler", "Cooler123", "Caller", "Caller123");

		Collections.shuffle(password);
		return password.get(0).trim();
	}

	/**
	 * 
	 * @return
	 */
	public static String genRandomMonth() {
		List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August",
				"September", "October", "November", "December");

		Collections.shuffle(months);
		return months.get(0).trim();
	}

}
