package com.marton.chocolate.service;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marton.chocolate.model.Chocolate;

import lombok.Data;

@Data
public class ChocolateResponse {
	private int page;
	@JsonProperty("per_page")
	private int perPage;
	private int total;
	@JsonProperty("total_pages")
	private int totalPages;
	private List<Chocolate> data;
}
