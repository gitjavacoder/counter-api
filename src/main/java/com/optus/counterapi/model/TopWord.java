package com.optus.counterapi.model;

import java.math.BigInteger;

import com.opencsv.bean.CsvBindByPosition;

public class TopWord {
	@CsvBindByPosition(position = 0)
	String word;
	
	@CsvBindByPosition(position = 1)
	BigInteger count;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}

}
