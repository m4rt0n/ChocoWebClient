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

	private static final String BASE_URL = "https://jsonmock.hackerrank.com/api/chocolates";
	private static final HttpClient CLIENT = HttpClient.newHttpClient();
	private static final ObjectMapper MAPPER = new ObjectMapper();

	// Collect chocolates per country
	private static List<Chocolate> fetchAllChocolates(String country) {
		List<Chocolate> allChocolates = new ArrayList<>();
		int currentPage = 1;
		int totalPages = 1;

		String encodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8);

		while (currentPage <= totalPages) {
			String url = buildUrl(encodedCountry, currentPage);
			String responseBody = fetchPage(url, currentPage);
			if (responseBody == null)
				break;

			ChocolateResponse response = parseResponse(responseBody, currentPage);
			if (response == null)
				break;

			if (response.getData() != null) {
				allChocolates.addAll(response.getData());
			}

			totalPages = response.getTotal_pages();
			currentPage++;
		}

		return allChocolates;
	}

	// Build URL String from country input
	private static String buildUrl(String encodedCountry, int page) {
		return String.format("%s?countryOfOrigin=%s&page=%s", BASE_URL, encodedCountry, page);
	}

	// Fetch page by country and page number
	private static String fetchPage(String url, int page) {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

		try {
			HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() != 200) {
				System.err.printf("Non-OK HTTP response (%d) on page %d%n", response.statusCode(), page);
				return null;
			}
			return response.body();
		} catch (IOException | InterruptedException e) {
			System.err.printf("HTTP error on page %d: %s%n", page, e.getMessage());
			return null;
		}
	}

	// Parse response
	private static ChocolateResponse parseResponse(String json, int page) {
		try {
			return MAPPER.readValue(json, ChocolateResponse.class);
		} catch (JsonMappingException e) {
			System.err.printf("JSON mapping error on page %d: %s%n", page, e.getMessage());
		} catch (IOException e) {
			System.err.printf("IO error during JSON parsing on page %d: %s%n", page, e.getMessage());
		}
		return null;
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
