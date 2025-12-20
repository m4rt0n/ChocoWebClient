package com.marton.chocolate.service;

import java.util.List;

import com.marton.chocolate.model.Chocolate;

import lombok.Data;

@Data
public class ChocolateResponse {
	private int page;
	private int per_page;
	private int total;
	private int total_pages;
	private List<Chocolate> data;
}
