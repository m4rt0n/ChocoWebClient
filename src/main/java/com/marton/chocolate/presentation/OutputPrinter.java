package com.marton.chocolate.presentation;

import java.util.Optional;

import com.marton.chocolate.model.Chocolate;

public class OutputPrinter {
	// chocolates.forEach(ch -> System.out.println(ch));

	// Get the most nutritious chocolate per given country
	public void printFindHighestKcalChocolate(Optional<Chocolate> chocolate) {
		chocolate.ifPresentOrElse(
				c -> System.out.println(String.format("Highest kcal chocolate: %s %s : %s kcal", c.getBrand(),
						c.getType(), c.getNutritionalInformation().getKcal())),
				() -> System.out.println("No valid kcal data found"));
	}
}
