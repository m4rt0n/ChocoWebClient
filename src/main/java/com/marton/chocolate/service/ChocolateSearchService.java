package com.marton.chocolate.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marton.chocolate.model.Chocolate;

public class ChocolateSearchService {
	private static final String BASE_URL = "https://jsonmock.hackerrank.com/api/chocolates";
	private static final HttpClient CLIENT = HttpClient.newHttpClient();
	private static final ObjectMapper MAPPER = new ObjectMapper();

	// Collect chocolates per country
	public List<Chocolate> fetchAllChocolates(String country) throws IOException {
		List<Chocolate> allChocolates = new ArrayList<>();
		int currentPage = 1;
		int totalPages = 1;

		String encodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8);

		while (currentPage <= totalPages) {
			String url = buildUrl(encodedCountry, currentPage);
			String responseBody = fetchPage(url, currentPage);

			ChocolateResponse response = parseResponse(responseBody, currentPage);

			if (response.getData() != null) {
				allChocolates.addAll(response.getData());
			}

			totalPages = response.getTotal_pages();
			currentPage++;
		}

		if (allChocolates.isEmpty()) {
			System.out.println("No chocolates found for country: " + country);
			return Collections.emptyList();
		}
		// Print results
		System.out.println(String.format("Total %s chocolates found from %s", allChocolates.size(), country));
		return allChocolates;
	}

	// Build URL String from country input
	public String buildUrl(String encodedCountry, int page) {
		return String.format("%s?countryOfOrigin=%s&page=%s", BASE_URL, encodedCountry, page);
	}

	// Fetch page by country and page number
	public String fetchPage(String url, int page) throws IOException {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

		try {
			HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.body().isEmpty()) {
				throw new IOException(String.format("Empty response body from %s", url));
			} else {
				if (response.statusCode() != 200) {
					throw new IOException(
							String.format("Non-OK HTTP response %s on page %s", response.statusCode(), page));
				}
				return response.body();
			}
		} catch (Exception e) {
			throw new IOException(String.format("HTTP error on page %d: %s%n", page, e.getMessage()));
		}
	}

	// Parse JSON response to ChocolateResponse POJO
	public ChocolateResponse parseResponse(String json, int page) throws IOException {
		try {
			return MAPPER.readValue(json, ChocolateResponse.class);
		} catch (JsonProcessingException e) {
			throw new IOException(String.format("JSON mapping/parsing error on page %d: %s%n", page, e.getMessage()));
		}
	}

}
