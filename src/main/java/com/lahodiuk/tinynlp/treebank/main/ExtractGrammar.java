package com.lahodiuk.tinynlp.treebank.main;

import java.util.Map.Entry;

import com.lahodiuk.tinynlp.treebank.visitors.GrammarExctractor;

public class ExtractGrammar {

	public static void main(String[] args) throws Exception {
		GrammarExctractor grammarExtractor = new GrammarExctractor();

		TreebankUtils.processPennTreebank(grammarExtractor);
		TreebankUtils.process4000QuestionsTreebank(grammarExtractor);
		TreebankUtils.processBNCTreebank(grammarExtractor);
		System.out.println();

		for (Entry<String, Integer> e : grammarExtractor.getRulesSortedByFrequency()) {
			if (e.getKey().contains("NONE") || e.getKey().contains("*")) {
				continue;
			}
			System.out.println(String.format("%5d\t%s", e.getValue(), e.getKey()));
		}
	}
}
