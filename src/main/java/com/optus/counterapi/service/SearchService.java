package com.optus.counterapi.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.optus.counterapi.model.SearchInput;
import com.optus.counterapi.model.SearchOutput;
import com.optus.counterapi.model.TopWord;

@Service
public class SearchService {

	Logger logger = LoggerFactory.getLogger(SearchService.class);

	@Value("${input.file}")
	String inputPath;

	/**
	 * @param searchInput
	 * @return String
	 * 
	 *  This method will return the number of times the words appear from the input file.
	 *  
	 */
	public String countWords(SearchInput searchInput) {
		SearchOutput searchOutput = new SearchOutput();
		Map<String, BigInteger> wordsMap = this.readAllWords();
		Map<String, BigInteger> countMap = new LinkedHashMap<String, BigInteger>();
		ArrayList<Map<String, BigInteger>> list = new ArrayList<Map<String, BigInteger>>();
		for (String word : searchInput.getSearchText()) {
			if (wordsMap.containsKey(word.toLowerCase())) {
				countMap.put(word, wordsMap.get(word.toLowerCase()));
			} else {
				countMap.put(word, BigInteger.valueOf(0));
			}
		}
		list.add(countMap);
		searchOutput.setCounts(countMap);

		return searchOutput.toString();

	}

	/**
	 * @param number
	 * @param response
	 * 
	 * This method will return the top words with most occurrences from the input file.
	 */
	public void topWords(Long number, HttpServletResponse response) {
		Map<String, BigInteger> wordsMap = this.readAllWords();
		List<TopWord> list = new ArrayList<TopWord>();

		Map<String, BigInteger> sortedMap = wordsMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(number)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		for (String key : sortedMap.keySet()) {
			TopWord topWord = new TopWord();
			topWord.setWord(key);
			topWord.setCount(sortedMap.get(key));
			list.add(topWord);

		}

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"result.csv\"");

		StatefulBeanToCsv<TopWord> writer;
		try {
			writer = new StatefulBeanToCsvBuilder<TopWord>(response.getWriter())
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
					.withSeparator('|').withOrderedResults(true)
					.build();

			writer.write(list);
		} catch (IOException e) {
			logger.error("Error in generating the CSV file!");
			e.printStackTrace();
		} catch (CsvDataTypeMismatchException e) {
			logger.error("Error in generating the CSV file!");
			e.printStackTrace();
		} catch (CsvRequiredFieldEmptyException e) {
			logger.error("Error in generating the CSV file!");
			e.printStackTrace();
		}
	}

	/**
	 * @return Map
	 * 
	 *  This method will read all words from the input file and count their occurrences as a Map data type.r
	 */
	private Map<String, BigInteger> readAllWords() {
		Map<String, BigInteger> wordsMap = new HashMap<String, BigInteger>();

		try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				String[] words = line.split(" ");
				for (int i = 0; i < words.length; i++) {
					words[i] = words[i].replace(",", "").replace(".", "");
					if (wordsMap.get(words[i].toLowerCase()) == null) {
						wordsMap.put(words[i].toLowerCase(), BigInteger.valueOf(1));
					} else {
						wordsMap.put(words[i].toLowerCase(),
								wordsMap.get(words[i].toLowerCase()).add(BigInteger.valueOf(1)));
					}
				}
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
		} catch (IOException e) {
			logger.error("Error in reading the input file!");
			e.printStackTrace();
		}

		return wordsMap;
	}
	
	

}
