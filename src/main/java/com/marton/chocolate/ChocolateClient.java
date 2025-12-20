package com.marton.chocolate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.marton.chocolate.model.Chocolate;
import com.marton.chocolate.presentation.ConsoleReader;
import com.marton.chocolate.presentation.OutputPrinter;
import com.marton.chocolate.service.ChocolateOperation;
import com.marton.chocolate.service.ChocolateSearchService;

public class ChocolateClient {
	public static void main(String[] args) throws IOException {
		// Read country from console
		ConsoleReader consoleReader = new ConsoleReader();
		String country = consoleReader.readInputCountry();
		ChocolateSearchService chocolateSearchService = new ChocolateSearchService();

		List<Chocolate> chocolates = chocolateSearchService.fetchAllChocolates(country);

		ChocolateOperation chocolateOperation = new ChocolateOperation();
		Optional<Chocolate> chocolateOptionalHighest = chocolateOperation.findHighestKcalChocolate(chocolates);

		OutputPrinter outputPrinter = new OutputPrinter();

		outputPrinter.printFindHighestKcalChocolate(chocolateOptionalHighest);
	}
}