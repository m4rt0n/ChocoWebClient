package com.marton.chocolate;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ChocolateOperation {

	// Optional because some Chocolate objects have no NutritionalInformation field
	// or kcal = null
	public Optional<Chocolate> findHighestKcalChocolate(List<Chocolate> chocolates) {
		if (chocolates == null || chocolates.isEmpty()) {
			return Optional.empty();
		}

		return chocolates.stream().filter(c -> c.getNutritionalInformation() != null)
				.filter(c -> c.getNutritionalInformation().getKcal() != null)
				.max(Comparator.comparingInt(c -> c.getNutritionalInformation().getKcal()));
	}

	// chocolates.forEach(ch -> System.out.println(ch));

	// Get the most nutritious chocolate per given country
	public void printHindHighestKcalChocolate(Optional<Chocolate> chocolate) {
		chocolate.ifPresentOrElse(
				c -> System.out.println(String.format("Highest kcal chocolate: %s %s : %s kcal", c.getBrand(),
						c.getType(), c.getNutritionalInformation().getKcal())),
				() -> System.out.println("No valid kcal data found"));
	}

}
