package com.marton.chocolate;

import java.util.Scanner;

public class ConsoleReader {

	public String readInputCountry() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter country of origin: ");
		String country = scanner.nextLine().trim();
		scanner.close();

		if (country.isEmpty()) {
			System.out.println("Country cannot be empty.");
			return null;
		}
		return country;
	}
}
