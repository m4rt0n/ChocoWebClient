package com.marton.chocolate.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.marton.chocolate.model.Chocolate;

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

}
