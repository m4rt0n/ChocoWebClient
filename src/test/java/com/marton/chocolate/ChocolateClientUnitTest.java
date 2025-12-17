package com.marton.chocolate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChocolateClientUnitTest {

	@Test
	@DisplayName("Should return chocolate with highest kcal value")
	void testFindHighestKcalChocolate_ReturnsMaxKcalChocolate() {
		// given
		NutritionalInformation n1 = new NutritionalInformation();
		n1.setKcal(200);

		NutritionalInformation n2 = new NutritionalInformation();
		n2.setKcal(450);

		Chocolate ch1 = new Chocolate();
		ch1.setBrand("Lindt");
		ch1.setNutritionalInformation(n1);

		Chocolate ch2 = new Chocolate();
		ch2.setBrand("Milka");
		ch2.setNutritionalInformation(n2);

		List<Chocolate> chocolates = List.of(ch1, ch2);

		// when
		Optional<Chocolate> result = invokeFindHighestKcalChocolate(chocolates);

		// then
		assertTrue(result.isPresent());
		assertEquals("Milka", result.get().getBrand());
		assertEquals(450, result.get().getNutritionalInformation().getKcal());
	}

	@Test
	@DisplayName("Should return empty when list is null or empty")
	void testFindHighestKcalChocolate_NullOrEmptyList() {
		assertTrue(invokeFindHighestKcalChocolate(null).isEmpty());
		assertTrue(invokeFindHighestKcalChocolate(List.of()).isEmpty());
	}

	@Test
	@DisplayName("Should skip chocolates with null nutritionalInformation or kcal")
	void testFindHighestKcalChocolate_SkipInvalidData() {
		Chocolate valid = new Chocolate();
		NutritionalInformation ni = new NutritionalInformation();
		ni.setKcal(300);
		valid.setNutritionalInformation(ni);

		Chocolate invalid1 = new Chocolate(); // null nutritional info
		Chocolate invalid2 = new Chocolate();
		invalid2.setNutritionalInformation(new NutritionalInformation()); // null kcal

		List<Chocolate> chocolates = List.of(invalid1, invalid2, valid);

		Optional<Chocolate> result = invokeFindHighestKcalChocolate(chocolates);

		assertTrue(result.isPresent());
		assertEquals(300, result.get().getNutritionalInformation().getKcal());
	}

	// Helper to access private static method reflectively
	private Optional<Chocolate> invokeFindHighestKcalChocolate(List<Chocolate> chocolates) {
		try {
			var method = ChocolateClient.class.getDeclaredMethod("findHighestKcalChocolate", List.class);
			method.setAccessible(true);
			@SuppressWarnings("unchecked")
			Optional<Chocolate> result = (Optional<Chocolate>) method.invoke(null, chocolates);
			return result;
		} catch (Exception e) {
			fail("Reflection call failed: " + e.getMessage());
			return Optional.empty();
		}
	}
}