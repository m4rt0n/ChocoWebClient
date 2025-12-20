package com.marton.chocolate.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Chocolate {
	private String type;
	private String brand;
	private List<String> ingredients;
	private List<Integer> prices;
	private List<Integer> weights;
	private String countryOfOrigin;
	private NutritionalInformation nutritionalInformation;
	private List<String> allergenInformation;
	private int productNumber;
	private List<String> flavors;
}
