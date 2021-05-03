package com.optus.counterapi.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.optus.counterapi.model.SearchInput;
import com.optus.counterapi.service.SearchService;

@RestController
@RequestMapping(path = "/counter-api")
public class CounterController {

	@Autowired
	SearchService searchService;

	/**
	 * @param searchInput
	 * @return String
	 * 
	 * This method will return the number of times the words appear from the input file.
	 */
	@PostMapping(value = "/search")
	public String search(@RequestBody SearchInput searchInput) {
		return this.searchService.countWords(searchInput);
	}

	/**
	 * @param number
	 * @param response
	 * 
	 * This method will return the top words with most occurrences from the input file.
	 */
	@GetMapping(value = "/top/{number}", produces = "text/csv")
	public void top(@PathVariable Long number, HttpServletResponse response) {
		this.searchService.topWords(number, response);
	}

}
