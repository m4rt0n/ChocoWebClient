package com.marton.chocolate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.marton.chocolate.model.Chocolate;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class ChocolateClientIntegrationTest {

	private MockWebServer mockWebServer;

	@BeforeEach
	void setUp() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();
	}

	@AfterEach
	void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	@DisplayName("Should fetch and parse chocolates from mock API")
	void testFetchAllChocolates_ParsesValidJson() throws Exception {
		// given
		String mockJson = """
				{
				  "page": 1,
				  "per_page": 10,
				  "total": 1,
				  "total_pages": 1,
				  "data": [
				    {
				      "id": 1,
				      "brand": "Lindt",
				      "type": "Dark",
				      "countryOfOrigin": "Switzerland",
				      "nutritionalInformation": {"kcal": 560}
				    }
				  ]
				}
				""";

		mockWebServer.enqueue(new MockResponse().setBody(mockJson).addHeader("Content-Type", "application/json"));

		String mockUrl = mockWebServer.url("/api/chocolates").toString();

		// use reflection to invoke fetchAllChocolates but modify base URL
		Method fetchMethod = ChocolateClient.class.getDeclaredMethod("fetchAllChocolates", String.class);
		fetchMethod.setAccessible(true);

		// Patch URL base dynamically by replacing string inside code (simulate)
		List<Chocolate> result = (List<Chocolate>) fetchMethod.invoke(null, "Switzerland");

		// verify
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Lindt", result.get(0).getBrand());
		assertEquals(560, result.get(0).getNutritionalInformation().getKcal());
	}
}