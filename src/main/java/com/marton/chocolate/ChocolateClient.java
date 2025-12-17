package com.marton.chocolate;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChocolateClient {
	public static void main(String[] args) {
		// Read country from console
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter country of origin: ");
		String country = scanner.nextLine().trim();
		scanner.close();

		if (country.isEmpty()) {
			System.out.println("Country cannot be empty.");
			return;
		}

		List<Chocolate> chocolates = fetchAllChocolates(country);

		if (chocolates.isEmpty()) {
			System.out.println("No chocolates found for country: " + country);
			return;
		}

		// Print results
		System.out.println(String.format("Total %s chocolates found from %s", chocolates.size(), country));
		// chocolates.forEach(ch -> System.out.println(ch));

		// Get the most nutritious chocolate per given country
		Optional<Chocolate> highestKcalChocolate = findHighestKcalChocolate(chocolates);
		highestKcalChocolate.ifPresentOrElse(
				c -> System.out.println(String.format("Highest kcal chocolate: %s %s : %s kcal", c.getBrand(),
						c.getType(), c.getNutritionalInformation().getKcal())),
				() -> System.out.println("No valid kcal data found"));
	}

	/**
	 * Fetches all pages of chocolates for a given country.
	 */

/// --- REFACTOR --- ///
	private static List<Chocolate> fetchAllChocolates(String country) {
		List<Chocolate> allChocolates = new ArrayList<>();
		HttpClient client = HttpClient.newHttpClient();

		ObjectMapper mapper = new ObjectMapper();

		int currentPage = 1;
		int totalPages = 1;

		String urlBase = "https://jsonmock.hackerrank.com/api/chocolates";
		// Encode country for URL safety
		String encodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8);

		while (currentPage <= totalPages) {
			String url = String.format("%s?countryOfOrigin=%s&page=%s", urlBase, encodedCountry, currentPage);
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

			HttpResponse<String> response;
			try {
				response = client.send(request, HttpResponse.BodyHandlers.ofString());
			} catch (IOException | InterruptedException e) {
				System.err.println(String.format("HTTP error on page %s : %s", currentPage, e.getMessage()));
				break;
			}

			if (response.statusCode() != 200) {
				System.err.println(
						String.format("Non-OK HTTP response (%s) on page %s", response.statusCode(), currentPage));
				break;
			}

			try {
				ChocolateResponse chocolateResponse = mapper.readValue(response.body(), ChocolateResponse.class);

				if (chocolateResponse.getData() != null) {
					allChocolates.addAll(chocolateResponse.getData());
				}

				totalPages = chocolateResponse.getTotal_pages();
			} catch (JsonMappingException e) {
				System.err.println(String.format("JSON mapping error on page %s : %s", currentPage, e.getMessage()));
				break;
			} catch (IOException e) {
				System.err.println(
						String.format("IO error during JSON parsing on page %s : %s", currentPage, e.getMessage()));
				break;
			}

			currentPage++;
		}

		return allChocolates;
	}

	// Optional because some Chocolate objects have no NutritionalInformation field
	// or kcal = null
	private static Optional<Chocolate> findHighestKcalChocolate(List<Chocolate> chocolates) {
		if (chocolates == null || chocolates.isEmpty()) {
			return Optional.empty();
		}

		return chocolates.stream().filter(c -> c.getNutritionalInformation() != null)
				.filter(c -> c.getNutritionalInformation().getKcal() != null)
				.max(Comparator.comparingInt(c -> c.getNutritionalInformation().getKcal()));
	}
}
