package com.optus.counterapi.model;

import java.math.BigInteger;
import java.util.Map;

public class SearchOutput {
	private Map<String, BigInteger> counts;

	public Map<String, BigInteger> getCounts() {
		return counts;
	}

	public void setCounts(Map<String, BigInteger> counts) {
		this.counts = counts;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		boolean start = true;
		builder.append("{");
		builder.append("\"counts\":");
		builder.append(" [");
		for (Map.Entry<String, BigInteger> key : counts.entrySet()) {
			if (!start) {
				builder.append(", ");
			} else {
				start = false;
			}
			builder.append("{");
			builder.append("\"" + key.getKey() + "\": ");
			builder.append(counts.get(key.getKey()));
			builder.append("}");
		}
		builder.append("]");
		builder.append("}");
		return builder.toString();
	}
}
