package com.marton.chocolate;

import java.util.List;
import java.util.Optional;

public class ChocolateClient {
	public static void main(String[] args) {
		// Read country from console
		ConsoleReader consoleReader = new ConsoleReader();
		String country = consoleReader.readInputCountry();
		ChocolateSearchService chocolateSearchService = new ChocolateSearchService();

		List<Chocolate> chocolates = chocolateSearchService.fetchAllChocolates(country);

		ChocolateOperation chocolateOperation = new ChocolateOperation();
		Optional<Chocolate> chocolateOptionalHighest = chocolateOperation.findHighestKcalChocolate(chocolates);
		chocolateOperation.printHindHighestKcalChocolate(chocolateOptionalHighest);
	}
}